package com.example.afreecar.model.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.AbstractEquatable;
import com.example.afreecar.model.Equatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

/**
 * Class containing information about a terminal's QR code ID and its target unique part tag.
 */
public class TerminalInfo extends AbstractEquatable<TerminalInfo> implements Parcelable {

    private ID id;
    private PartTag target;

    public TerminalInfo(@NonNull ID terminalID, @NonNull PartTag targetPartTag) {
        this.id = terminalID;
        this.target = targetPartTag;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected TerminalInfo(Parcel in) {
        id = in.readParcelable(ID.class.getClassLoader());
        target = in.readParcelable(PartTag.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeParcelable(target, flags);
    }

    public static final Creator<TerminalInfo> CREATOR = new Creator<TerminalInfo>() {
        @Override
        public TerminalInfo createFromParcel(Parcel in) {
            return new TerminalInfo(in);
        }

        @Override
        public TerminalInfo[] newArray(int size) {
            return new TerminalInfo[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    public ID getID() {
        return id;
    }

    public PartTag getTarget() {
        return target;
    }

    @Override
    public boolean equals(TerminalInfo other) {
        return this.id.equals(other.id) && this.target.equals(other.target);
    }

    @Override
    public TerminalInfo clone() {
        return new TerminalInfo(this.id.clone(), this.target.clone());
    }
}
