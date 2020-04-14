package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;

import javax.annotation.concurrent.Immutable;

/**
 * Class containing information about a terminal's QR code ID, intended distance from the edge of another terminal, and its target unique part tag.
 */
@Immutable
public class Terminal extends AbstractAssemblyItem<Terminal> implements Parcelable {

    public static final String COLLECTION_NAME = "Terminals";
    public static final String ID_FIELD_NAME = "id";
    public static final String QR_DISTANCE_FIELD_NAME = "qr distance";
    public static final String TARGET_TAG_FIELD_NAME = "target tag";

    @NonNull private PartTag target;

    public Terminal(ID terminalID, Double qrDistance, PartTag targetPartTag) {
        super(terminalID, qrDistance);
        this.target = targetPartTag;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected Terminal(Parcel in) {
        super(in);
        target = in.readTypedObject(PartTag.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedObject(target, flags);
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

    public PartTag getTarget() {
        return target;
    }

    @Override
    public boolean equals(Terminal other) {
        Boolean result;
        result = super.equals(other);
        result &= this.target.equals(other.target);
        return result;
    }

    @Override
    public Terminal clone() {
        return new Terminal(getID(), getQRDistance(), this.target.clone());
    }
}
