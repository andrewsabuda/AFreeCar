package com.example.afreecar.model;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.assembly.Part;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an eKit, which is defined by a kit ID and a map of PartTags to their chosen Parts
 */
public class Kit extends AbstractEquatable<Kit> {


    @NonNull private final ID kitID;
    @NonNull private final Map<PartTag, Part> partsMap;

    public Kit(ID kitID, Map<PartTag, Part> partsMap) {
        this.kitID = kitID;
        this.partsMap = new HashMap<>(partsMap);
    }

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
    public boolean equals(Kit other) {
        Boolean result;

        result = this.kitID.equals(other.kitID);
        result &= this.partsMap.equals(other.partsMap);

        return result;
    }

    @Override
    public Kit clone() {
        return new Kit(getID(), partsMap);
    }
}
