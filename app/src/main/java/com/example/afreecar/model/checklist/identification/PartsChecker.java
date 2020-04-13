package com.example.afreecar.model.checklist.identification;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.KitRequirements;
import com.example.afreecar.model.checklist.AbstractChecklist;
import com.example.afreecar.model.checklist.PartTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to retrieve a set of parts for the input eKit ID,
 * and then construct a collection of valid scanned QR code IDs from those sets.
 */
public class PartsChecker extends AbstractChecklist<PartsChecker, PartTag, ID> implements Parcelable {

    private Map<PartTag, ID> pickedPartsMap;
    private Map<PartTag, PartRequirement> validPartsMap;

    private PartsChecker(Map<PartTag, ID> pickedPartsMap, Map<PartTag, PartRequirement> validPartsMap) {
        this.pickedPartsMap = pickedPartsMap;
        this.validPartsMap = validPartsMap;
    }

    public PartsChecker(KitRequirements kitRequirements) {

        validPartsMap = Collections.unmodifiableMap(kitRequirements.getPartRequirements());
        pickedPartsMap = new HashMap<PartTag, ID>(validPartsMap.size());

        for (PartTag tag: validPartsMap.keySet()) {
            pickedPartsMap.put(tag, null);
        }
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartsChecker(Parcel in){
        int length = in.readInt();

        PartTag[] partTags = new PartTag[length];
        in.readTypedArray(partTags, PartTag.CREATOR);

        ID[] pickedIDs = new ID[length];
        in.readTypedArray(pickedIDs, ID.CREATOR);

        ID[][] validIDSets = new ID[length][];

        int i;
        for (i = 0; i < length; i++) {
            validIDSets[i] = in.createTypedArray(ID.CREATOR);
        }

        pickedPartsMap = new HashMap<PartTag, ID>(length);
        validPartsMap = new HashMap<PartTag, PartRequirement>(length);

        for (i = 0; i < length; i++) {
            PartTag currentTag = partTags[i];
            pickedPartsMap.put(partTags[i], pickedIDs[i]);

            Set<ID> tempValidIDSet = new HashSet<ID>(length);
            ID[] readValidIDArray = validIDSets[i];
            for(ID validID: readValidIDArray) {
                tempValidIDSet.add(validID);
            }

            validPartsMap.put(currentTag, new PartRequirement(currentTag, tempValidIDSet));
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

            Set<ID> tempPickedIDSet = validPartsMap.get(partTags[i]).getValidPartIDs();
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
            return new PartsChecker(in);
        }

        @Override
        public PartsChecker[] newArray(int size) {
            return new PartsChecker[size];
        }
    };


    // END PARCELABLE IMPLEMENTATION



    // BEGIN CHECKLIST IMPLEMENTATION

    /**
     * @return the set of part tags required for assembling the eKit, for use in displaying in UI.
     */
    @Override
    public List<PartTag> getElements() {
        ArrayList<PartTag> partTags;
        partTags = new ArrayList<PartTag>(pickedPartsMap.keySet());
        Collections.sort(partTags);
        return partTags;
    }

    @Override
    public Boolean doesFulfill(PartTag partTag, ID partID) {
        Boolean result;

        result = validPartsMap.get(partTag).getValidPartIDs().contains(partID);

        return result;
    }

    /**
     * @param partTag - the {@code PartTag} whose respective value is intended to be set.
     * @param partID - the {@code PartID} that will be checked and, if found to be valid, inserted into the map.
     * @return true if the input {@code ID} was found to be valid, indicating that data has been updated;
     * false otherwise.
     */
    @Override
    public Boolean tryFulfill(PartTag partTag, ID partID) {
        if (this.doesFulfill(partTag, partID)) {
            pickedPartsMap.put(partTag, partID);
            return true;
        }

        return false;
    }

    /**
     * @param partTag - the {@code PartTag} whose respective value should be checked.
     * @return true if the input {@code PartTag} was found to have a chosen {@code ID} associated with it.
     */
    @Override
    public Boolean isFulfilled(PartTag partTag) {
        return pickedPartsMap.get(partTag) != null;
    }

    // END CHECKLIST IMPLEMENTATION

    /**
     * @param partTag - The PartTag for which the currently selected ID should be retrieved
     * @return The part ID currently selected for this PartTag
     */
    public ID getPickedPartID(PartTag partTag) {
        return pickedPartsMap.get(partTag);
    }

    /**
     * @param partTag - The PartTag for which the set of valid part IDs should be retrieved
     * @return the set of valid part IDs for this PartTag
     */
    public Set<ID> getValidParts(PartTag partTag) {
        return validPartsMap.get(partTag).getValidPartIDs();
    }

    /**
     * @param partTag - the {@code PartTag} whose respective value should be set to null.
     * @return true if there was an entry for the input {@code PartTag},
     * indicating that data has been updated; false otherwise.
     */
    public boolean tryRemovePart(PartTag partTag) {
        if (isFulfilled(partTag)) {
            pickedPartsMap.put(partTag, null);
            return true;
        }

        return false;
    }

    @Override
    public PartsChecker clone() {
        return new PartsChecker(new HashMap<PartTag, ID>(pickedPartsMap), new HashMap<PartTag, PartRequirement>(validPartsMap));
    }

    @Override
    public boolean equals(PartsChecker other) {
        return this.validPartsMap.equals(other.validPartsMap) && this.pickedPartsMap.equals(other.pickedPartsMap);
    }

    /**
     * @return a copy of the pickedPartsMap.
     */
    public Map<PartTag, ID> clonePickedParts() {
        return Collections.unmodifiableMap(pickedPartsMap);
    }
}
