package com.example.afreecar.model.identification;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.DataAccessUtils;
import com.example.afreecar.model.DemoStuff;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to retrieve a set of parts for the input eKit ID,
 * and then construct a collection of valid scanned QR code IDs from those sets.
 */
public class PartsChecker implements Parcelable {

    private Map<PartTag, ID> pickedPartsMap;
    private Map<PartTag, Set<ID>> validPartsMap;

    // Essentially only here to aid in parcelable constructor
    private PartsChecker(int length, PartTag[] partTags, ID[] pickedIDs, ID[][] validIDSets) throws Exception {
        if (length != partTags.length || length != pickedIDs.length || length != validIDSets.length) {
            throw new Exception("Input array lengths must match all match length parameter");
        }

        pickedPartsMap = new HashMap<PartTag, ID>(length);
        validPartsMap = new HashMap<PartTag, Set<ID>>(length);

        for (int i = 0; i < length; i++) {
            pickedPartsMap.put(partTags[i], pickedIDs[i]);

            Set<ID> tempValidIDSet = new HashSet<ID>(length);
            ID[] readValidIDArray = validIDSets[i];
            for(ID validID: readValidIDArray) {
                tempValidIDSet.add(validID);
            }

            validPartsMap.put(partTags[i], tempValidIDSet);
        }
    }

    public PartsChecker(PartRequirement[] partRequirements) {

        validPartsMap = new HashMap<PartTag, Set<ID>>(partRequirements.length);
        pickedPartsMap = new HashMap<PartTag, ID>(partRequirements.length);

        for (PartRequirement partRequirement: partRequirements) {
            validPartsMap.put(partRequirement.getPartTag(), partRequirement.getValidPartIDs());
            pickedPartsMap.put(partRequirement.getPartTag(), null);
        }
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartsChecker(Parcel in) throws Exception {
        int length = in.readInt();

        PartTag[] partTags = new PartTag[length];
        in.readTypedArray(partTags, PartTag.CREATOR);

        ID[] pickedIDs = new ID[length];
        in.readTypedArray(pickedIDs, ID.CREATOR);

        ID[][] validIDSets = new ID[length][];

        int i;
        for (i = 0; i < length; i++) {
            in.readTypedArray(validIDSets[i], ID.CREATOR);
        }

        pickedPartsMap = new HashMap<PartTag, ID>(length);
        validPartsMap = new HashMap<PartTag, Set<ID>>(length);

        for (i = 0; i < length; i++) {
            pickedPartsMap.put(partTags[i], pickedIDs[i]);

            Set<ID> tempValidIDSet = new HashSet<ID>(length);
            ID[] readValidIDArray = validIDSets[i];
            for(ID validID: readValidIDArray) {
                tempValidIDSet.add(validID);
            }

            validPartsMap.put(partTags[i], tempValidIDSet);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int length = validPartsMap.size();

        PartTag[] partTags = validPartsMap.keySet().toArray(new PartTag[length]);

        ID[] pickedIDs = new ID[length];
        ID[][] validIDs = new ID[length][];

        int i;
        for (i = 0; i < length; i++) {
            pickedIDs[i] = pickedPartsMap.get(partTags[i]); // Put entries of pickedPartsMap in same order as keys

            Set<ID> tempPickedIDSet = validPartsMap.get(partTags[i]);
            validIDs[i] = tempPickedIDSet.toArray(new ID[tempPickedIDSet.size()]); // Ditto for validPartsMap
        }

        dest.writeInt(length); // Write size of both maps
        dest.writeTypedArray(partTags, flags); // Write keys for both maps
        dest.writeTypedArray(pickedIDs, flags); // Write picked part IDs

        for (i = 0; i < length; i++) {
            dest.writeTypedArray(validIDs[i], flags); // Write array of valid part IDs
        }
    }

    public static final Creator<PartsChecker> CREATOR = new Creator<PartsChecker>() {
        @Override
        public PartsChecker createFromParcel(Parcel in) {
            try {
                return new PartsChecker(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public PartsChecker[] newArray(int size) {
            return new PartsChecker[size];
        }
    };


    // END PARCELABLE IMPLEMENTATION

    /**
     * @return the set of part tags required for assembling the eKit, for use in displaying in UI.
     */
    public Set<PartTag> getPartTags() {
        return pickedPartsMap.keySet();
    }

    /**
     * @param partTag - The PartTag for which the currently selected ID should be retrieved
     * @return The part ID currently selected for this PartTag
     */
    public ID getPickedPart(PartTag partTag) {
        return pickedPartsMap.get(partTag);
    }

    /**
     *
     * @param partTag - the {@code PartTag} whose respective value should be checked.
     * @return true if the input {@code PartTag} was found to have a chosen {@code ID} associated with it.
     */
    public boolean isPartPicked(PartTag partTag) {
        return pickedPartsMap.get(partTag) != null;
    }

    /**
     * @param partTag - the {@code PartTag} whose respective value is intended to be set.
     * @param partID - the {@code PartID} that will be checked and, if found to be valid, inserted into the map.
     * @return true if the input {@code ID} was found to be valid, indicating that data has been updated;
     * false otherwise.
     */
    public boolean tryPickPart(PartTag partTag, ID partID) {
        if (validPartsMap.get(partTag).contains(partID)) {
            pickedPartsMap.put(partTag, partID);
            return true;
        }

        return false;
    }

    /**
     * @param partTag - the {@code PartTag} whose respective value should be set to null.
     * @return true if there was an entry for the input {@code PartTag},
     * indicating that data has been updated; false otherwise.
     */
    public boolean tryRemovePart(PartTag partTag) {
        if (isPartPicked(partTag)) {
            pickedPartsMap.put(partTag, null);
            return true;
        }

        return false;
    }

    /**
     * @return true if an {@code ID} for a part has been chosen to fulfill every part requirement; false otherwise.
     */
    public boolean isFulfilled() {
        for (PartTag partTag: pickedPartsMap.keySet()) {
            if (!isPartPicked(partTag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return a copy of the pickedPartsMap.
     */
    public Map<PartTag, ID> clonePickedParts() {
        return new HashMap<PartTag, ID>(pickedPartsMap);
    }
}