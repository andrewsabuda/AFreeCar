package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.google.common.primitives.UnsignedInteger;

/**
 * Class containing information about a terminal's QR code ID and its target unique part tag.
 */
public class Terminal extends AbstractAssemblyItem<Terminal> implements Parcelable {

    private PartTag target;

    public Terminal(@NonNull ID terminalID, @NonNull Double qrDistance, @NonNull PartTag targetPartTag) {
        super(terminalID, qrDistance);
        this.target = targetPartTag;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected Terminal(Parcel in) {
        super(in);
        target = in.readTypedObject(PartTag.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        return super.equals(other) && this.target.equals(other.target);
    }

    @Override
    public Terminal clone() {
        return new Terminal(getID(), getQRDistance(), this.target.clone());
    }
}
