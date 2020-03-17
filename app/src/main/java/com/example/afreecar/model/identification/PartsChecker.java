package com.example.afreecar.model.identification;

import androidx.annotation.NonNull;

import com.example.afreecar.model.DemoStuff;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PartsChecker {

    private ID kitID;
    private Map<PartTag, Set<ID>> validPartsMap;
    private Map<PartTag, ID> pickedPartsMap;

    public PartsChecker(@NonNull ID kitID) {
        this.kitID = kitID;

        PartRequirement[] temp = getPartRequirements(kitID);

        validPartsMap = new HashMap<PartTag, Set<ID>>(temp.length);
        pickedPartsMap = new HashMap<PartTag, ID>(temp.length);

        for (PartRequirement partRequirement: temp) {
            validPartsMap.put(partRequirement.getPartTag(), partRequirement.getValidPartIDs());
            pickedPartsMap.put(partRequirement.getPartTag(), null);
        }
    }

    /**
     * @param kitID - the ID of the kit whose requirements should be retreived.
     * @return an array of {@code PartRequirements} for the input KitID.
     */
    protected PartRequirement[] getPartRequirements(@NonNull ID kitID) {
        List<PartRequirement> temp = new ArrayList<>(3);

        if (kitID.equals(DemoStuff.kitID)) {
            temp = Arrays.asList(DemoStuff.controllerReq, DemoStuff.batteryReq, DemoStuff.motorReq);
        }

        return temp.toArray(new PartRequirement[temp.size()]);
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

    protected Map<PartTag, ID> getPickedParts() {
        return pickedPartsMap;
    }
}