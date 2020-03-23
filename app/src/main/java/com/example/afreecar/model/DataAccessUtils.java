package com.example.afreecar.model;

import androidx.annotation.NonNull;

import com.example.afreecar.model.DemoStuff;
import com.example.afreecar.model.assembly.PartConnectionsInfo;
import com.example.afreecar.model.assembly.TerminalInfo;
import com.example.afreecar.model.identification.PartRequirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class containing static methods used to query the database / get demo data.
 */
public class DataAccessUtils {

    /**
     * @param kitID - the ID of the kit whose requirements should be retreived.
     * @return an array of {@code PartRequirements} for the input KitID.
     */
    public static PartRequirement[] getPartRequirements(@NonNull ID kitID) {
        List<PartRequirement> temp = new ArrayList<>(3);

        if (kitID.equals(DemoStuff.kitID)) {
            temp = Arrays.asList(DemoStuff.controllerReq, DemoStuff.batteryReq, DemoStuff.motorReq);
        }

        return temp.toArray(new PartRequirement[temp.size()]);
    }

    /**
     * @param id - The {@Code ID} of the target part
     * @return The {@Code PartConnectionsInfo} containing the set of terminals associated with this part
     */
    public static PartConnectionsInfo getPartConnectionInfoForID(@NonNull ID id) {
        boolean needsChassisConnection = true;
        Set<TerminalInfo> terminals = new HashSet<TerminalInfo>();

        if (id.equals(DemoStuff.controllerID)) {
            terminals = DemoStuff.controllerTerminals;
        }
        else if (id.equals(DemoStuff.batteryID)) {
            terminals = DemoStuff.batteryTerminals;
        }
        else if (id.equals(DemoStuff.motorID)) {
            terminals = DemoStuff.motorTerminals;
        }
        else {
            needsChassisConnection = false;
        }

        return new PartConnectionsInfo(needsChassisConnection, terminals);
    }
}
