package com.example.afreecar.model;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;

/**
 * The {@code PartTag} class provides a separate ID type to delineate unique parts within a kit -
 * which may include multiple of the same type - which will be displayed to users when selecting parts.
 * Designed for use as a key in HashMaps as the primary front facing indexer for
 */
public class PartTag extends AbstractChecklistItem implements Comparable<PartTag> {

    private PartType type;
    private Byte ordinal;

    public static final int Cardinality = PartType.values().length * Byte.MAX_VALUE;

    /**
     * @param type - the type of part being described in this tag.
     * @param number - the ID relative to other parts of the same type in a kit.
     */
    public PartTag(@NonNull PartType type, @NonNull int number) {
        super(false);

        if (number < 1) {
            throw new InvalidParameterException("Ordinal must be at least 1");
        }

        if (number > Byte.MAX_VALUE) {
            throw new InvalidParameterException("Ordinal must be less than " + Byte.MAX_VALUE);
        }

        this.type = type;
        this.ordinal = (byte) number;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartTag(Parcel in) {
        super(false);
        type = in.readParcelable(PartType.class.getClassLoader());
        if (in.readByte() == 0) {
            ordinal = null;
        } else {
            ordinal = in.readByte();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(type, flags);
        if (ordinal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeByte(ordinal);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartTag> CREATOR = new Creator<PartTag>() {
        @Override
        public PartTag createFromParcel(Parcel in) {
            return new PartTag(in);
        }

        @Override
        public PartTag[] newArray(int size) {
            return new PartTag[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    public PartType getType() {
        return type;
    }

    public Byte getOrdinal() {
        return ordinal;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == PartTag.class ? this.equals((PartTag) other) : this.equals(other);
    }

    public boolean equals(PartTag other) {
        return type == other.type && ordinal == other.ordinal;
    }

    @Override
    public int hashCode() {
        return (type.ordinal() * Byte.MAX_VALUE) + ordinal - 1;
    }

    @Override
    public int compareTo(@NonNull PartTag other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        return type.toString() + " " + ordinal.toString();
    }
}
