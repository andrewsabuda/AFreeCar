package com.example.afreecar.model.identification;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to indicate a specific part required in a kit, and a list of valid part IDs that that may
 * fulfill that requirement.  Intended for use as a wrapper for data passed from the database.
 */
public class PartRequirement extends AbstractEquatable<PartRequirement> implements Parcelable {

    private PartTag partTag;
    private Set<ID> validPartIDs;

    /**
     * @param partTag - the {@code PartTag} whose requirement is being fulfilled.
     * @param validPartIDs - the set of valid part {@code ID}s for this particular requirement.
     */
    public PartRequirement(@NonNull PartTag partTag, @NonNull Set<ID> validPartIDs) {

        if (validPartIDs.size() < 1) {
            throw new InvalidParameterException("The set of valid parts must have at least one element.");
        }
        this.partTag = partTag;
        this.validPartIDs = validPartIDs;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartRequirement(Parcel in) {
        partTag = in.readParcelable(PartTag.class.getClassLoader());

        validPartIDs = new HashSet<ID>(Arrays.asList(in.createTypedArray(ID.CREATOR)));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(partTag, flags);
        dest.writeTypedArray(validPartIDs.toArray(new ID[validPartIDs.size()]), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartRequirement> CREATOR = new Creator<PartRequirement>() {
        @Override
        public PartRequirement createFromParcel(Parcel in) {
            return new PartRequirement(in);
        }

        @Override
        public PartRequirement[] newArray(int size) {
            return new PartRequirement[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    /**
     * @return the {@code PartTag} whose requirement is being fulfilled.
     */
    protected PartTag getPartTag() {
        return partTag;
    }

    /**
     * @return the set of valid part {@code ID}s for this particular requirement.
     */
    protected Set<ID> getValidPartIDs() {
        return validPartIDs;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == PartRequirement.class
                ? this.equals((PartRequirement) other)
                : this == other;
    }

    @Override
    public boolean equals(PartRequirement other) {
        return this.partTag.equals(other.partTag) && this.validPartIDs.equals(other.validPartIDs);
    }

    @Override
    public PartRequirement clone() {
        return new PartRequirement(this.partTag.clone(), new HashSet<ID>(validPartIDs));
    }
}
