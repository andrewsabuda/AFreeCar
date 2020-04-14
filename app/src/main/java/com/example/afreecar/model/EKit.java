package com.example.afreecar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.assembly.Part;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

/**
 * Represents an eKit, which is defined by a kit ID and a map of PartTags to their chosen Parts
 */
@Immutable
public class EKit extends AbstractEquatable<EKit> implements Parcelable {

    @NonNull private final ID kitID;
    @NonNull private final Map<PartTag, Part> partsMap;

    public EKit(ID kitID, Map<PartTag, Part> partsMap) {
        this.kitID = kitID.clone();
        this.partsMap = new HashMap<>(partsMap);
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected EKit(Parcel in) {
        kitID = in.readTypedObject(ID.CREATOR);

        PartTag[] partTags = in.createTypedArray(PartTag.CREATOR);
        Part[] parts = in.createTypedArray(Part.CREATOR);
        int length = partTags.length;

        partsMap = new HashMap<PartTag, Part>(length);
        for(int i = 0; i < length; i++) {
            partsMap.put(partTags[i], parts[i]);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(kitID, flags);

        Set<PartTag> partTags = partsMap.keySet();
        int length = partTags.size();
        PartTag[] partTagsArray = partTags.toArray(new PartTag[length]);
        dest.writeTypedArray(partTagsArray, flags);

        Part[] partsArray = partsMap.values().toArray(new Part[length]);
        dest.writeTypedArray(partsArray, flags);
    }

    public static final Creator<EKit> CREATOR = new Creator<EKit>() {
        @Override
        public EKit createFromParcel(Parcel in) {
            return new EKit(in);
        }

        @Override
        public EKit[] newArray(int size) {
            return new EKit[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION



    /**
     * Retrieves this Kit's ID
     * @return a clone of this Kit's ID
     */
    public ID getID() {
        return kitID.clone();
    }

    /**
     * Retrieves this Kit's map of PartTags to Parts
     * @return an unmodifiable clone of this Kit's map of PartTags to Parts
     */
    public Map<PartTag, Part> getPartsMap() {
        return Collections.unmodifiableMap(partsMap);
    }

    @Override
    public boolean equals(EKit other) {
        Boolean result;

        result = super.equals(other);
        result &= this.kitID.equals(other.kitID);
        result &= this.partsMap.equals(other.partsMap);

        return result;
    }

    @Override
    public EKit clone() {
        return new EKit(this.kitID, this.partsMap);
    }
}
