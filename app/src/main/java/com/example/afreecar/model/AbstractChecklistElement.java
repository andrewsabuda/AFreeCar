package com.example.afreecar.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public abstract class AbstractChecklistElement<T extends AbstractChecklistElement<T>> extends AbstractPerfectHashable<T> implements Parcelable {

    private boolean isPair;

    public AbstractChecklistElement(boolean isPair) {
        this.isPair = isPair;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected AbstractChecklistElement(Parcel in) {
        this(in.readBoolean());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(isPair);
        if (isPair) {
            ((PartTagPair) this).writeToParcel(dest, flags);
        }
        else {
            ((PartTag) this).writeToParcel(dest, flags);
        }
    }

    public static final Creator<AbstractChecklistElement> CREATOR = new Creator<AbstractChecklistElement>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public AbstractChecklistElement createFromParcel(Parcel in) {
            boolean isPair = in.readBoolean();
            in.setDataPosition(0);
            if (isPair) {
                return new PartTagPair(in);
            }
            else {
                return new PartTag(in);
            }
        }

        @Override
        public AbstractChecklistElement[] newArray(int size) {
            return new AbstractChecklistElement[size];
        }
    };

    // BEGIN PARCELABLE IMPLEMENTATION

    public boolean isPair() {
        return isPair;
    }

    // Breaks if you remove it
    @Override
    public abstract T clone();

    @Override
    public abstract String toString();
}
