package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.PartTagPair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to store information on the users progress towards assembling the complete kit.
 */
@Deprecated
public class PartsAssembly extends AbstractEquatable<PartsAssembly> implements Parcelable {

    // Maps tags for unique parts to their IDs
    @NonNull private final Map<PartTag, Part> partsMap;

    // Maps pairs of tags for unique parts that should be connected to each other, to their connection terminal IDs.
    @NonNull private final Map<PartTagPair, QRCodePair> terminalPairsMap;

    // Maps pairs of tags for unique parts that should be connected to each other, to the status of their connection.
    @NonNull private final Map<PartTagPair, Boolean> terminalConnectionStatusMap;

    // Maps tags for unique parts that should be physically connected to the chassis, to the status of that connection.
    @NonNull private final Map<PartTag, Boolean> chassisConnectionStatusMap;

    private PartsAssembly(
            Map<PartTag, Part> partsMap,
            Map<PartTagPair, QRCodePair> terminalIDPairsMap,
            Map<PartTagPair, Boolean> terminalConnectionStatusMap,
            Map<PartTag, Boolean> chassisConnectionStatusMap) {
        this.partsMap = partsMap;
        this.terminalPairsMap = terminalIDPairsMap;
        this.terminalConnectionStatusMap = terminalConnectionStatusMap;
        this.chassisConnectionStatusMap = chassisConnectionStatusMap;
    }

    public PartsAssembly(@NonNull Map<PartTag, Part> pickedPartsMap) {

        // Get the set of unique part tags
        Set<PartTag> partTags = pickedPartsMap.keySet();
        int size = partTags.size();

        // Initialize backing maps
        partsMap = Collections.unmodifiableMap(pickedPartsMap);
        terminalPairsMap = new HashMap<>(size);
        terminalConnectionStatusMap = new HashMap<>(size);
        chassisConnectionStatusMap = new HashMap<>(size);

        // Get connection requirements for each part, put starting entries in chassisConnectionStatusMap
        for (PartTag partTag : partTags) {

            // Query for PartConnectionsInfo data access object based on chosen ID for this unique part, put in map

            // Initialize entries with false values if needed
            if (partsMap.get(partTag).getQRDistance() > 0) {
                chassisConnectionStatusMap.put(partTag, false);
            }
        }

        // Iterate over set of PartTags used as keys in the map of all PartConnectionInfo entities
        for (PartTag partTag : partTags) {

            // Get terminal IDs map this part
            Map<PartTag, Terminal> thisPartTerminalMap = partsMap.get(partTag).getTerminalMap();

            // Iterate over set of tags for this part's associated terminal target parts
            for (PartTag terminalTag : thisPartTerminalMap.keySet()) {

                // Get terminalIDsMap for the PartConnectionInfo associated with the current terminal tag
                Map<PartTag, Terminal> otherPartTerminalMap = partsMap.get(terminalTag).getTerminalMap();

                // Check if the terminalIDsMap for the PartConnectionInfo associated with this terminal tag contains the tag for this part
                if (partsMap.get(terminalTag).getTerminalMap().containsKey(partTag)) {

                    // Construct new key for maps
                    PartTagPair newKey = new PartTagPair(partTag, terminalTag);

                    // Start entry in terminalConnectionStatusMap for new key with false value -
                    // validation unnecessary since value is the same
                    terminalConnectionStatusMap.put(newKey, false);

                    // Check if terminalIDPairsMap already contains value for this key, skip if it does
                    if (terminalPairsMap.containsKey(newKey)) {

                        // Construct new TerminalIDPair value by getting value for current terminal tag from outer terminal IDs map,
                        // and value for current part tag from inner terminal IDs map
                        terminalPairsMap.put(newKey, new QRCodePair(
                                thisPartTerminalMap.get(terminalTag),
                                otherPartTerminalMap.get(partTag)
                                ));
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
        this(new HashMap<PartTag, Part>());
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
    public boolean tryUpdateTerminalConnectionStatus(PartTagPair partTagPair, QRCodePair terminalIDPair, boolean value) {

        // Check if the input pair of terminal IDs matches the recorded pair
        if (terminalIDPair.equals(terminalPairsMap.get(partTagPair))) {
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
        if (partsMap.get(partTag).getID().equals(partID)) {
            chassisConnectionStatusMap.put(partTag, value);

            return true;
        }

        return false;
    }

    @Override
    public boolean equals(PartsAssembly other) {
        Boolean result;

        result = this.partsMap.equals(other.partsMap);
        result &= this.terminalPairsMap.equals(other.terminalPairsMap);
        result &= this.terminalConnectionStatusMap.equals(other.terminalConnectionStatusMap);
        result &= this.chassisConnectionStatusMap.equals(other.chassisConnectionStatusMap);

        return result;
    }

    @Override
    public PartsAssembly clone() {
        return new PartsAssembly(
                new HashMap<PartTag, Part>(partsMap),
                new HashMap<PartTagPair, QRCodePair>(terminalPairsMap),
                new HashMap<PartTagPair, Boolean>(terminalConnectionStatusMap),
                new HashMap<PartTag, Boolean>(chassisConnectionStatusMap));
    }
}
