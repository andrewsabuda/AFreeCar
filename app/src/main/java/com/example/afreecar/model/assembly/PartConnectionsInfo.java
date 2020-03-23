package com.example.afreecar.model.assembly;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class a collection of IDs of a part's terminals,
 * indexed by the unique part tag whose respective terminal they are intended to connect to,
 * as well as a boolean indicating whether or not the part must be physically connected to the chassis.
 * Intended as wrapper for data passed from the database.
 */
public class PartConnectionsInfo {
    private Map<PartTag, ID> terminalIDsMap;

    private boolean needsChassisConnection;

    public PartConnectionsInfo(@NonNull boolean needsChassisConnection, @NonNull Set<TerminalInfo> terminals) {
        this.needsChassisConnection = needsChassisConnection;

        terminalIDsMap = new HashMap<>(terminals.size());
        for (TerminalInfo terminal: terminals) {
            terminalIDsMap.put(terminal.getTarget(), terminal.getID());
        }
    }

    public Map<PartTag, ID> getTerminalIDsMap() {
        return terminalIDsMap;
    }

    public boolean getChassisConnectionRequirement() {
        return needsChassisConnection;
    }
}
