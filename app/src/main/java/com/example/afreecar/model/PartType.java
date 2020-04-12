package com.example.afreecar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Enumeration describing all possible types of parts that may be found in an eKit.
 */
public enum PartType implements Parcelable {
    Controller,
    Battery,
    Motor;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Writes enum ordinal as int to parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    // Uses stored ordinal to retrieve enum
    public static final Creator<PartType> CREATOR = new Creator<PartType>() {

        @Override
        public PartType createFromParcel(Parcel in) {
            return PartType.values()[in.readInt()];
        }

        @Override
        public PartType[] newArray(int size) {
            return new PartType[size];
        }
    };
}
