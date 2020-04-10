package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class intended to represent a collection of IDs of a part's terminals,
 * indexed by the unique part tag whose respective terminal they are intended to connect to,
 * as well as a boolean indicating whether or not the part must be physically connected to the chassis.
 * Intended as wrapper for data passed from the database.
 */
public class PartConnectionsInfo extends AbstractEquatable<PartConnectionsInfo> implements Parcelable {


    private boolean needsChassisConnection;
    private Map<PartTag, ID> terminalIDsMap;

    private PartConnectionsInfo(boolean needsChassisConnection, Map<PartTag, ID> terminalIDsMap) {
        this.needsChassisConnection = needsChassisConnection;
        this.terminalIDsMap = terminalIDsMap;
    }

    public PartConnectionsInfo(@NonNull boolean needsChassisConnection, @NonNull Set<Terminal> terminals) {
        this.needsChassisConnection = needsChassisConnection;

        terminalIDsMap = new HashMap<>(terminals.size());
        for (Terminal terminal: terminals) {
            terminalIDsMap.put(terminal.getTarget(), terminal.getID());
        }
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected PartConnectionsInfo(Parcel in) {
        needsChassisConnection = in.readBoolean();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(needsChassisConnection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartConnectionsInfo> CREATOR = new Creator<PartConnectionsInfo>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public PartConnectionsInfo createFromParcel(Parcel in) {
            return new PartConnectionsInfo(in);
        }

        @Override
        public PartConnectionsInfo[] newArray(int size) {
            return new PartConnectionsInfo[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    public Map<PartTag, ID> getTerminalIDsMap() {
        return terminalIDsMap;
    }

    public boolean getChassisConnectionRequirement() {
        return needsChassisConnection;
    }

    @Override
    public boolean equals(PartConnectionsInfo other) {
        return this.needsChassisConnection == other.needsChassisConnection;
    }

    @Override
    public PartConnectionsInfo clone() {
        return new PartConnectionsInfo(this.needsChassisConnection, this.terminalIDsMap);
    }
}
