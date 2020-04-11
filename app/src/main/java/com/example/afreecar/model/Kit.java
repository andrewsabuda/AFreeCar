package com.example.afreecar.model;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Kit extends AbstractEquatable<Kit> {

    public static final String COLLECTION_NAME = "Kits";
    public static final String ID_FIELD_NAME = "id";
    public static final String PART_REQS_FIELD_NAME = "part requirements";

    private final ID kitID;
    private final Map<PartTag, PartRequirement> partRequirements;

    public Kit(ID kitID, Map<PartTag, PartRequirement> partRequirements) {
        this.kitID = kitID.clone();
        this.partRequirements = Collections.unmodifiableMap(partRequirements);
    }

    public Kit(ID kitID, Set<PartRequirement> partRequirementSet) {
        this(kitID, toMap(partRequirementSet));
    }

    public Kit(ID kitID, PartRequirement... partRequirements) {
        this(kitID, toMap(partRequirements));
    }

    private static Map<PartTag, PartRequirement> toMap(Set<PartRequirement> partRequirementSet) {
        Map<PartTag, PartRequirement> output = new HashMap<PartTag, PartRequirement>(partRequirementSet.size());

        for(PartRequirement req: partRequirementSet) {
            output.put(req.getPartTag(), req);
        }

        return output;
    }

    private static Map<PartTag, PartRequirement> toMap(PartRequirement... partRequirements) {
        Map<PartTag, PartRequirement> output = new HashMap<PartTag, PartRequirement>(partRequirements.length);

        for(PartRequirement req: partRequirements) {
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
    public boolean equals(Kit other) {
        boolean output;
        output = this.kitID.equals(other.kitID);
        output &= this.partRequirements.equals(other.partRequirements);
        return output;
//        return this.kitID.equals(other.kitID) && this.partRequirements.equals(other.partRequirements);
    }

    @Override
    public Kit clone() {
        return new Kit(kitID, partRequirements);
    }
}
