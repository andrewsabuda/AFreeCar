package com.example.afreecar.model;

import com.example.afreecar.model.checklist.identification.PartRequirement;

import java.util.HashSet;
import java.util.Set;

public class Kit {

    public static final String COLLECTION_NAME = "kits";
    public static final String ID_FIELD_NAME = "id";
    public static final String PART_REQS_FIELD_NAME = "part requirements";

    private ID kitID;
    private Set<PartRequirement> partRequirements;

    public Kit(ID kitID, Set<PartRequirement> partRequirements) {
        this.kitID = kitID;
        this.partRequirements = partRequirements;
    }

    public ID getKitID() {
        return kitID.clone();
    }

    public Set<PartRequirement>  getPartRequirements() {
        return new HashSet<PartRequirement>(partRequirements);
    }
}
