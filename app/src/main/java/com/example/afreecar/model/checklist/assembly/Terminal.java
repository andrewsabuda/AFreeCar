package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;

/**
 * Class containing information about a terminal's QR code ID and its target unique part tag.
 */
public class Terminal extends AbstractEquatable<Terminal> implements Parcelable {

    private ID id;
    private PartTag target;

    public Terminal(@NonNull ID terminalID, @NonNull PartTag targetPartTag) {
        this.id = terminalID;
        this.target = targetPartTag;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected Terminal(Parcel in) {
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

    public static final Creator<Terminal> CREATOR = new Creator<Terminal>() {
        @Override
        public Terminal createFromParcel(Parcel in) {
            return new Terminal(in);
        }

        @Override
        public Terminal[] newArray(int size) {
            return new Terminal[size];
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
    public boolean equals(Terminal other) {
        return this.id.equals(other.id) && this.target.equals(other.target);
    }

    @Override
    public Terminal clone() {
        return new Terminal(this.id.clone(), this.target.clone());
    }
}
