package com.example.afreecar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.abstraction.AbstractPerfectHashable;

/**
 * Wrapper class for IDs to be read from QR codes, in case we use something besides {@code String}s.
 */
public class ID extends AbstractPerfectHashable<ID> implements Parcelable {

    private String id;

    public ID(@NonNull String id) {
        this.id = id;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected ID(Parcel in) {
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    public static final Creator<ID> CREATOR = new Creator<ID>() {
        @Override
        public ID createFromParcel(Parcel in) {
            return new ID(in);
        }

        @Override
        public ID[] newArray(int size) {
            return new ID[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    @Override
    public boolean equals(ID other) {
        return this.id.equals(other.id);
    }

    @Override
    public ID clone() {
        return new ID(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
