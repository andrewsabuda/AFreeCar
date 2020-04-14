package com.example.afreecar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

@Immutable
public class KitRequirements extends AbstractEquatable<KitRequirements> implements Parcelable {

    public static final String COLLECTION_NAME = "Kits";
    public static final String ID_FIELD_NAME = "id";
    public static final String PART_REQS_FIELD_NAME = "part requirements";

    @NonNull private final ID kitID;
    @NonNull private final Map<PartTag, PartRequirement> partRequirements;

    public KitRequirements(ID kitID, Map<PartTag, PartRequirement> partRequirements) {
        this.kitID = kitID.clone();
        this.partRequirements = new HashMap<PartTag, PartRequirement>(partRequirements);
    }

    public KitRequirements(ID kitID, Collection<PartRequirement> partRequirementSet) {
        this(kitID, toMap(partRequirementSet));
    }

    public KitRequirements(ID kitID, PartRequirement... partRequirements) {
        this(kitID, Arrays.asList(partRequirements));
    }



    // BEGIN PARCELABLE IMPLEMENTATION

    protected KitRequirements(Parcel in) {
        this(in.readTypedObject(ID.CREATOR), in.createTypedArrayList(PartRequirement.CREATOR));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(kitID, flags);

        PartRequirement[] partReqsArray;
        Collection<PartRequirement> partReqsColl = partRequirements.values();
        partReqsArray = partReqsColl.toArray(new PartRequirement[partReqsColl.size()]);
        dest.writeTypedArray(partReqsArray, flags);
    }

    public static final Creator<KitRequirements> CREATOR = new Creator<KitRequirements>() {
        @Override
        public KitRequirements createFromParcel(Parcel in) {
            return new KitRequirements(in);
        }

        @Override
        public KitRequirements[] newArray(int size) {
            return new KitRequirements[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION



    private static Map<PartTag, PartRequirement> toMap(Collection<PartRequirement> partRequirementSet) {
        Map<PartTag, PartRequirement> output = new HashMap<PartTag, PartRequirement>(partRequirementSet.size());

        for(PartRequirement req: partRequirementSet) {
            output.put(req.getPartTag(), req);
        }

        return output;
    }

    public ID getKitID() {
        return kitID.clone();
    }

    public Map<PartTag, PartRequirement>  getPartRequirements() {
        return Collections.unmodifiableMap(partRequirements);
    }

    @Override
    public boolean equals(KitRequirements other) {
        boolean output;

        output = this.kitID.equals(other.kitID);
        output &= this.partRequirements.equals(other.partRequirements);

        return output;
    }

    @Override
    public KitRequirements clone() {
        return new KitRequirements(kitID, partRequirements);
    }
}
