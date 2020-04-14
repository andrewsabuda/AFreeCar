package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.EKit;
import com.example.afreecar.model.checklist.AbstractChecklist;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.PartTagPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssemblyChecklist extends AbstractChecklist<AssemblyChecklist, PartTagPair, QRCodePair> implements Parcelable {

    private static final Double ACCEPTABLE_DISTANCE_DELTA = 0.5;

    @NonNull private final Map<PartTagPair, QRCodePair> validPairsMap;
    @NonNull private final Map<PartTagPair, Boolean> fulfilledPairsMap;

    private AssemblyChecklist(Map<PartTagPair, QRCodePair> validPairsMap, Map<PartTagPair, Boolean> fulfilledPairsMap) {
        this.validPairsMap = new HashMap<PartTagPair, QRCodePair>(validPairsMap);
        this.fulfilledPairsMap = new HashMap<PartTagPair, Boolean>(fulfilledPairsMap);
    }

    public AssemblyChecklist(EKit chosenKit) {
        // Get the set of unique part tags
        Map<PartTag, Part> partsMap = chosenKit.getPartsMap();
        Set<PartTag> partTags = partsMap.keySet();
        int size = partTags.size();

        this.validPairsMap = new HashMap<PartTagPair, QRCodePair>(size);
        this.fulfilledPairsMap = new HashMap<PartTagPair, Boolean>(size);

        // Iterate over set of PartTags used as keys in the map of all Part entities
        for (PartTag currentPartTag : partTags) {

            // Update maps with Part-Chassis connection
            putPart(this.validPairsMap, this.fulfilledPairsMap, partsMap, currentPartTag);

            putTerminalPair(this.validPairsMap, this.fulfilledPairsMap, partsMap, currentPartTag);
        }
    }



    // BEGIN PARCELABLE IMPLEMENTATION

    protected AssemblyChecklist(Parcel in) {
        PartTagPair[] partTagPairs = in.createTypedArray(PartTagPair.CREATOR);
        QRCodePair[] qrCodePairs = in.createTypedArray(QRCodePair.CREATOR);
        boolean[] fulfillmentStatuses = in.createBooleanArray();
        int length = partTagPairs.length;

        this.validPairsMap = new HashMap<PartTagPair, QRCodePair>(length);
        this.fulfilledPairsMap = new HashMap<PartTagPair, Boolean>(length);

        for (int i = 0; i < partTagPairs.length; i++) {
            PartTagPair current = partTagPairs[i];
            validPairsMap.put(current, qrCodePairs[i]);
            fulfilledPairsMap.put(current, fulfillmentStatuses[i]);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PartTagPair[] partTagPairs;
        QRCodePair[] qrCodePairs;
        boolean[] fulfillmentStatuses;

        Set<PartTagPair> partTagPairSet = this.validPairsMap.keySet();
        int size = partTagPairSet.size();
        partTagPairs = new PartTagPair[size];
        qrCodePairs = new QRCodePair[size];
        fulfillmentStatuses = new boolean[size];

        int i = 0;
        for(PartTagPair element: partTagPairSet) {
            partTagPairs[i] = element;
            qrCodePairs[i] = this.validPairsMap.get(element);
            fulfillmentStatuses[i] = this.fulfilledPairsMap.get(element);
            ++i;
        }


        dest.writeTypedArray(partTagPairs, flags);
        dest.writeTypedArray(qrCodePairs, flags);
        dest.writeBooleanArray(fulfillmentStatuses);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AssemblyChecklist> CREATOR = new Creator<AssemblyChecklist>() {
        @Override
        public AssemblyChecklist createFromParcel(Parcel in) {
            return new AssemblyChecklist(in);
        }

        @Override
        public AssemblyChecklist[] newArray(int size) {
            return new AssemblyChecklist[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION



    private static void putPart(Map<PartTagPair, QRCodePair> validPairsMap, Map<PartTagPair, Boolean> fulfilledPairsMap, Map<PartTag, Part> partsMap, PartTag partTag) {
        PartTagPair partChassisPair = new PartTagPair(partTag);

        Part targetPart = partsMap.get(partTag);
        QRCodePair partChassisDistance = new QRCodePair(targetPart);

        validPairsMap.put(partChassisPair, partChassisDistance);
        fulfilledPairsMap.put(partChassisPair, false);
    }

    private static void putTerminalPair(Map<PartTagPair, QRCodePair> validPairsMap, Map<PartTagPair, Boolean> fulfilledPairsMap, Map<PartTag, Part> partsMap, PartTag partTag) {
        // Get terminal IDs map this part
        Map<PartTag, Terminal> currentPartsTerminalMap = partsMap.get(partTag).getTerminalMap();

        // Iterate over set of tags for this part's associated terminal target parts
        for (PartTag currentTerminalTag : currentPartsTerminalMap.keySet()) {

            // Get terminalIDsMap for the PartConnectionInfo associated with the current terminal tag
            Map<PartTag, Terminal> targetPartsTerminalMap = partsMap.get(currentTerminalTag).getTerminalMap();

            // Check if the terminalIDsMap for the PartConnectionInfo associated with this terminal tag contains the tag for this part
            if (partsMap.get(currentTerminalTag).getTerminalMap().containsKey(partTag)) {

                // Construct new key for maps
                PartTagPair terminalTagPair = new PartTagPair(partTag, currentTerminalTag);

                // Start entry in terminalConnectionStatusMap for new key with false value
                fulfilledPairsMap.put(terminalTagPair, false);

                // Construct new TerminalIDPair value by getting value for current terminal tag from outer terminal IDs map,
                // and value for current part tag from inner terminal IDs map
                QRCodePair terminalQRDistance = matchTerminals(currentPartsTerminalMap, targetPartsTerminalMap, partTag, currentTerminalTag);

                validPairsMap.put(terminalTagPair, terminalQRDistance);

            }
            else {
                throw new IllegalStateException("Part cannot be missing a terminal connection targeted at another part which has one targeting the source part.");
            }
        }
    }

    private static QRCodePair matchTerminals(Map<PartTag, Terminal> partOnesTerminalMap, Map<PartTag, Terminal> partTwosTerminalTag, PartTag terminalTargetingPartOnesTag, PartTag terminalTargetingPartTwosTag) {
        QRCodePair terminalQRDistance;

        Terminal partOnesTerminalForPartTwo = partOnesTerminalMap.get(terminalTargetingPartTwosTag);
        Terminal partTwosTerminalForPartOne = partTwosTerminalTag.get(terminalTargetingPartOnesTag);

        terminalQRDistance = new QRCodePair(partOnesTerminalForPartTwo, partTwosTerminalForPartOne);

        return terminalQRDistance;
    }

    public Map<PartTagPair, QRCodePair> getValidDistancesMap() {
        return Collections.unmodifiableMap(this.validPairsMap);
    }

    public Map<PartTagPair, Boolean> getFulfilledElementsMap() {
        return Collections.unmodifiableMap(this.fulfilledPairsMap);
    }

    @Override
    public boolean equals(AssemblyChecklist other) {
        Boolean result;
        result = super.equals(other);
        result &= this.validPairsMap.equals(other.validPairsMap);
        result &= this.fulfilledPairsMap.equals(other.fulfilledPairsMap);
        return result;
    }

    @Override
    public AssemblyChecklist clone() {
        return new AssemblyChecklist(this.validPairsMap, this.fulfilledPairsMap);
    }

    @Override
    public List<PartTagPair> getElements() {
        List<PartTagPair> elements;
        elements = new ArrayList<PartTagPair>(validPairsMap.keySet());
        return elements;
    }

    @Override
    public Boolean doesFulfill(PartTagPair element, QRCodePair fulfillObject) {
        Boolean result;

        Double validQRDistance = validPairsMap.get(element).getQRDistance();
        Double fulfillQRDistance = fulfillObject.getQRDistance();

        result = isCloseEnough(validQRDistance, fulfillQRDistance, ACCEPTABLE_DISTANCE_DELTA);

        return result;
    }

    private static Boolean isCloseEnough(Double expected, Double actual, Double delta) {
        Boolean result;
        // Make sure actual value is above lower bound
        result = actual >= (expected - delta);
        // Make sure actual value is also below upper bound
        result &= actual <= (expected + delta);
        return result;
    }

    @Override
    public Boolean tryFulfill(PartTagPair element, QRCodePair fulfillObject) {
        Boolean doesFulfill = doesFulfill(element, fulfillObject);
        this.fulfilledPairsMap.put(element, doesFulfill);
        return doesFulfill;
    }

    @Override
    public Boolean isFulfilled(PartTagPair element) {
        return this.fulfilledPairsMap.get(element);
    }
}
