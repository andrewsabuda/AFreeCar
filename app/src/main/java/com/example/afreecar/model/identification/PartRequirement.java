package com.example.afreecar.model.identification;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import java.security.InvalidParameterException;
import java.util.Set;

/**
 * Class to indicate a specific part required in a kit, and a list of valid part IDs that that may
 * fulfill that requirement.  Intended for use as a wrapper for data passed from the database.
 */
public class PartRequirement {

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
}
