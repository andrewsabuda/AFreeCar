package com.example.afreecar.model.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.afreecar.model.AbstractEquatable;
import com.example.afreecar.model.DataAccessUtils;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;
import com.example.afreecar.model.PartTagPair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to store information on the users progress towards assembling the complete kit.
 */
public class PartsAssembly extends AbstractEquatable<PartsAssembly> implements Parcelable {

    // Maps tags for unique parts to their IDs
    Map<PartTag, ID> partIDsMap;

    // Maps pairs of tags for unique parts that should be connected to each other, to their connection terminal IDs.
    Map<PartTagPair, TerminalIDPair> terminalIDPairsMap;

    // Maps pairs of tags for unique parts that should be connected to each other, to the status of their connection.
    Map<PartTagPair, Boolean> terminalConnectionStatusMap;

    // Maps tags for unique parts that should be physically connected to the chassis, to the status of that connection.
    Map<PartTag, Boolean> chassisConnectionStatusMap;

    private PartsAssembly(
            Map<PartTag, ID> partIDsMap,
            Map<PartTagPair, TerminalIDPair> terminalIDPairsMap,
            Map<PartTagPair, Boolean> terminalConnectionStatusMap,
            Map<PartTag, Boolean> chassisConnectionStatusMap) {
        this.partIDsMap = partIDsMap;
        this.terminalIDPairsMap = terminalIDPairsMap;
        this.terminalConnectionStatusMap = terminalConnectionStatusMap;
        this.chassisConnectionStatusMap = chassisConnectionStatusMap;
    }

    public PartsAssembly(Map<PartTag, ID> pickedPartsMap) {

        // Get the set of unique part tags
        Set<PartTag> partTags = pickedPartsMap.keySet();
        int size = partTags.size();

        // Initialize backing maps
        partIDsMap = new HashMap<>(pickedPartsMap);
        terminalIDPairsMap = new HashMap<>(size);
        terminalConnectionStatusMap = new HashMap<>(size);
        chassisConnectionStatusMap = new HashMap<>(size);

        // Get connection requirements for each part, put starting entries in chassisConnectionStatusMap
        Map<PartTag, PartConnectionsInfo> allPartConnectionsInfo = new HashMap<>(size);
        for (PartTag partTag : partTags) {

            // Query for PartConnectionsInfo data access object based on chosen ID for this unique part, put in map
            allPartConnectionsInfo.put(partTag, DataAccessUtils.getPartConnectionInfoForID(pickedPartsMap.get(partTag)));

            // Initialize entries with false values if needed
            if (allPartConnectionsInfo.get(partTag).getChassisConnectionRequirement()) {
                chassisConnectionStatusMap.put(partTag, false);
            }
        }

        // Iterate over set of PartTags used as keys in the map of all PartConnectionInfo entities
        for (PartTag partTag : partTags) {

            // Get terminal IDs map this part
            Map<PartTag, ID> thisPartsTerminalIDsMap = allPartConnectionsInfo.get(partTag).getTerminalIDsMap();

            // Iterate over set of tags for this part's associated terminal target parts
            for (PartTag terminalTag : thisPartsTerminalIDsMap.keySet()) {

                // Get terminalIDsMap for the PartConnectionInfo associated with the current terminal tag
                Map<PartTag, ID> otherPartsTerminalIDsMap = allPartConnectionsInfo.get(terminalTag).getTerminalIDsMap();

                // Check if the terminalIDsMap for the PartConnectionInfo associated with this terminal tag contains the tag for this part
                if (allPartConnectionsInfo.get(terminalTag).getTerminalIDsMap().containsKey(partTag)) {

                    // Construct new key for maps
                    PartTagPair newKey = new PartTagPair(partTag, terminalTag);

                    // Start entry in terminalConnectionStatusMap for new key with false value -
                    // validation unnecessary since value is the same
                    terminalConnectionStatusMap.put(newKey, false);

                    // Check if terminalIDPairsMap already contains value for this key, skip if it does
                    if (terminalIDPairsMap.containsKey(newKey)) {

                        // Construct new TerminalIDPair value by getting value for current terminal tag from outer terminal IDs map,
                        // and value for current part tag from inner terminal IDs map
                        terminalIDPairsMap.put(newKey, new TerminalIDPair(
                                thisPartsTerminalIDsMap.get(terminalTag),
                                otherPartsTerminalIDsMap.get(partTag)));
                    }
                }
                else {
                    throw new IllegalStateException("Part cannot be missing a terminal connection targeted at another part which has one targeting the source part.");
                }
            }
        }
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartsAssembly(Parcel in) {
        //Todo
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Todo
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartsAssembly> CREATOR = new Creator<PartsAssembly>() {
        @Override
        public PartsAssembly createFromParcel(Parcel in) {
            return new PartsAssembly(in);
        }

        @Override
        public PartsAssembly[] newArray(int size) {
            return new PartsAssembly[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    /**
     * @param partTagPair - The pair of tags for the unique parts whose terminal-terminal connection status should be retrieved.
     * @return the current status of the input terminal-terminal connection.
     */
    public boolean getTerminalConnectionStatus(PartTagPair partTagPair) {
        return terminalConnectionStatusMap.get(partTagPair);
    }

    /**
     * Updates the status of a terminal-terminal connection with the input value if and only if the input {@Code TerminalIDPair} matches that recorded in the terminalIDPairsMap.
     * @param partTagPair - The pair of tags for the unique parts whose terminal-terminal connection should be updated.
     * @param terminalIDPair - The pair of {@Code ID}s scanned, to be matched against the recorded {@Code ID}s of the terminals connecting the input parts.
     * @param value - The truth value that the entry in terminalConnectionStatusMap will be updated to reflect if validations pass.
     * @return true if the input {@Code TerminalIDPair} passed validation, indicating that the backing map has been updated to hold the input value; false otherwise.
     */
    public boolean tryUpdateTerminalConnectionStatus(PartTagPair partTagPair, TerminalIDPair terminalIDPair, boolean value) {

        // Check if the input pair of terminal IDs matches the recorded pair
        if (terminalIDPair.equals(terminalIDPairsMap.get(partTagPair))) {
            terminalConnectionStatusMap.put(partTagPair, value);

            return true;
        }

        return false;
    }

    /**
     * @param partTag - The tag for the unique part whose requirement of physical to the chassis should be retrieved.
     * @return whether or not the input part requires a physical connection to the chassis.
     */
    public boolean needsChassisConnection(PartTag partTag) {
        return chassisConnectionStatusMap.containsKey(partTag);
    }

    /**
     *
     * @param partTag - The tag for the unique part whose status of physical connection to the chassis should be retrieved.
     * @return the current status of the input part-chassis connection.
     */
    public boolean getChassisConnectionStatus(PartTag partTag) {
        return chassisConnectionStatusMap.get(partTag);
    }

    /**
     * Updates the status of a part's physical connection to the chassis if and only if the input {@Code ID} matches that recorded in the partIDsMap
     * @param partTag - The tag for the unique part whose physical connection to the chassis should be updated.
     * @param partID - The scanned {@Code ID} of the part, to be matched against the recorded {@Code ID} of the part.
     * @param value - The truth value that the entry in chassisConnectionStatusMap will be updated to reflect if validations pass.
     * @return true if the input {@Code ID} passed validation, indicating that the backing map has been updated to hold the input value; false otherwise.
     */
    public boolean tryUpdateChassisConnectionStatus(PartTag partTag, ID partID, boolean value) {

        // Check if the input part ID matches the recorded ID for the part tag
        if (partIDsMap.get(partTag).equals(partID)) {
            chassisConnectionStatusMap.put(partTag, value);

            return true;
        }

        return false;
    }

    @Override
    public boolean equals(PartsAssembly other) {
        return this.partIDsMap.equals(other.partIDsMap)
                && this.terminalIDPairsMap.equals(other.terminalIDPairsMap)
                && this.terminalConnectionStatusMap.equals(other.terminalConnectionStatusMap)
                && this.chassisConnectionStatusMap.equals(other.chassisConnectionStatusMap);
    }

    @Override
    public PartsAssembly clone() {
        return new PartsAssembly(
                new HashMap<PartTag, ID>(partIDsMap),
                new HashMap<PartTagPair, TerminalIDPair>(terminalIDPairsMap),
                new HashMap<PartTagPair, Boolean>(terminalConnectionStatusMap),
                new HashMap<PartTag, Boolean>(chassisConnectionStatusMap));
    }
}
