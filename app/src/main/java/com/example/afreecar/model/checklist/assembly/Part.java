package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class intended to represent a collection of IDs of a part's terminals,
 * indexed by the unique part tag whose respective terminal they are intended to connect to,
 * as well as a boolean indicating whether or not the part must be physically connected to the chassis.
 * Intended as wrapper for data passed from the database.
 */
public class Part extends AbstractAssemblyItem<Part> {

    @NonNull private final Map<PartTag, Terminal> terminalMap;

    private Part(ID partID, Double qrDistance, Map<PartTag, Terminal>terminalMap) {
        super(partID, qrDistance);
        this.terminalMap = terminalMap;
    }

    public Part(ID partID, Double qrDistance, Collection<Terminal> terminals) {
        this(partID, qrDistance, toMap(terminals));
    }

    public Part(ID partID, Double qrDistance, Terminal... terminals) {
        this(partID, qrDistance, toMap(terminals));
    }

    private static Map<PartTag, Terminal> toMap(Collection<Terminal> terminals) {
        Map<PartTag, Terminal> terminalIDsMap = new HashMap<>(terminals.size());
        for (Terminal terminal: terminals) {
            terminalIDsMap.put(terminal.getTarget(), terminal);
        }

        return terminalIDsMap;
    }

    private static Map<PartTag, Terminal> toMap(Terminal... terminals) {
        Map<PartTag, Terminal> terminalIDsMap = new HashMap<>(terminals.length);
        for (Terminal terminal: terminals) {
            terminalIDsMap.put(terminal.getTarget(), terminal);
        }

        return terminalIDsMap;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected Part(Parcel in) {
        super(in);
        this.terminalMap = toMap(in.createTypedArray(Terminal.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedArray(terminalMap.values().toArray(new Terminal[terminalMap.size()]), flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Part> CREATOR = new Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel in) {
            return new Part(in);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    public Map<PartTag, Terminal> getTerminalMap() {
        return terminalMap;
    }

    @Override
    public boolean equals(Part other) {
        boolean output;
        output = super.equals(other);
        output &= this.terminalMap.equals(other.terminalMap);
        return output;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), terminalMap);
    }

    @Override
    public Part clone() {
        return new Part(this.getID(), this.getQRDistance(), this.terminalMap);
    }
}
