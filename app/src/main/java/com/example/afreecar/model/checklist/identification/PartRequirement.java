package com.example.afreecar.model.checklist.identification;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.google.common.collect.ImmutableSet;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to indicate a specific part required in a kit, and a list of valid part IDs that that may
 * fulfill that requirement.  Intended for use as a wrapper for data passed from the database.
 */
public class PartRequirement extends AbstractEquatable<PartRequirement> implements Parcelable {

    public static final String COLLECTION_NAME = "PartRequirements";
    public static final String PART_TAG_FIELD_NAME = "part tag";
    public static final String VALID_IDS_FIELD_NAME = "valid ids";

    private final PartTag partTag;
    private final Set<ID> validPartIDs;

    /**
     * @param partTag - the {@code PartTag} whose requirement is being fulfilled.
     * @param validPartIDs - the set of valid part {@code ID}s for this particular requirement.
     */
    public PartRequirement(@NonNull PartTag partTag, @NonNull Set<ID> validPartIDs) {

        if (validPartIDs.size() < 1) {
            throw new InvalidParameterException("The set of valid parts must have at least one element.");
        }
        this.partTag = partTag;
        this.validPartIDs = Collections.unmodifiableSet(validPartIDs);
    }

    public PartRequirement(@NonNull PartTag partTag, @NonNull ID... validPartIDs) {
        this(partTag, new HashSet<ID>(Arrays.asList(validPartIDs)));
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
    public PartTag getPartTag() {
        return partTag;
    }

    /**
     * @return the set of valid part {@code ID}s for this particular requirement.
     */
    public Set<ID> getValidPartIDs() {
        return Collections.unmodifiableSet(validPartIDs);
    }

    @Override
    public int hashCode() {
        int hash;

        hash = partTag.hashCode();
        for(ID validID: validPartIDs) {
            hash *= validID.hashCode();
        }

        return hash;
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
