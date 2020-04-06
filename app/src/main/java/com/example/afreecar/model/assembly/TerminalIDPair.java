package com.example.afreecar.model.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.AbstractEquatable;
import com.example.afreecar.model.ID;

public class TerminalIDPair extends AbstractEquatable<TerminalIDPair> implements Parcelable {

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

    protected TerminalIDPair(Parcel in) {
        one = in.readParcelable(ID.class.getClassLoader());
        two = in.readParcelable(ID.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(one, flags);
        dest.writeParcelable(two, flags);
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
    public TerminalIDPair clone() {
        return new TerminalIDPair(one.clone(), two.clone());
    }

    @Override
    public boolean equals(TerminalIDPair other) {
        return this.one.equals(other.one) && this.two.equals(other.two);
    }
}
