package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.afreecar.model.abstraction.AbstractHashable;
import com.example.afreecar.model.ID;

public class TerminalIDPair extends AbstractHashable<TerminalIDPair> implements Parcelable {

    private ID one;
    private ID two;

    public TerminalIDPair(@NonNull ID one, @NonNull ID two) {
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
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected TerminalIDPair(Parcel in) {
        one = in.readTypedObject(ID.CREATOR);
        two = in.readTypedObject(ID.CREATOR);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(one, flags);
        dest.writeTypedObject(two, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TerminalIDPair> CREATOR = new Creator<TerminalIDPair>() {
        @Override
        public TerminalIDPair createFromParcel(Parcel in) {
            return new TerminalIDPair(in);
        }

        @Override
        public TerminalIDPair[] newArray(int size) {
            return new TerminalIDPair[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    @Override
    public int hashCode() {
        return one.hashCode() * two.hashCode();
    }

    @Override
    public TerminalIDPair clone() {
        return new TerminalIDPair(one.clone(), two.clone());
    }

    @Override
    public boolean equals(TerminalIDPair other) {
        return this.one.equals(other.one) && this.two.equals(other.two);
    }

    @Override
    public int compareTo(TerminalIDPair other) {
        int oneComparison = this.one.compareTo(other.one);
        return oneComparison == 0
                ? this.two.compareTo(other.two)
                : oneComparison;
    }
}
