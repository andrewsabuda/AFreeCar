package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.afreecar.model.abstraction.AbstractHashable;
import com.example.afreecar.model.ID;

import java.util.Objects;

public class TerminalPair extends AbstractHashable<TerminalPair> implements Parcelable {

    private final ID one;
    private final ID two;
    private final Double qrDistance;

    public TerminalPair(@NonNull ID one, @NonNull ID two, @NonNull Double qrDistance) {
        // Ensures sorting of parts internally
        int comparison = one.compareTo(two);
        if (comparison == 0) {
            throw new IllegalArgumentException("Cannot pair a terminal to itself.");
        }
        else if (comparison < 0) {
            this.one = one;
            this.two = two;
        }
        else {
            this.one = two;
            this.two = one;
        }

        this.qrDistance = qrDistance;
    }

    public TerminalPair(@NonNull Terminal one, @NonNull Terminal two) {
        this(one.getID(),two.getID(),one.getQRDistance() + two.getQRDistance());
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected TerminalPair(Parcel in) {
        this(in.readTypedObject(ID.CREATOR), in.readTypedObject(ID.CREATOR), in.readDouble());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(one, flags);
        dest.writeTypedObject(two, flags);
        dest.writeDouble(qrDistance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TerminalPair> CREATOR = new Creator<TerminalPair>() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public TerminalPair createFromParcel(Parcel in) {
            return new TerminalPair(in);
        }

        @Override
        public TerminalPair[] newArray(int size) {
            return new TerminalPair[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(one.toString(), two.toString(), qrDistance);
    }

    @Override
    public TerminalPair clone() {
        return new TerminalPair(one.clone(), two.clone(), Double.valueOf(qrDistance));
    }

    @Override
    public boolean equals(TerminalPair other) {
        return this.one.equals(other.one) && this.two.equals(other.two);
    }

    @Override
    public int compareTo(TerminalPair other) {
        int oneComparison = this.one.compareTo(other.one);
        return oneComparison == 0
                ? this.two.compareTo(other.two)
                : oneComparison;
    }
}
