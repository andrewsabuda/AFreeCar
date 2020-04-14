package com.example.afreecar.model.checklist;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.afreecar.model.PartType;

import java.security.InvalidParameterException;

import javax.annotation.concurrent.Immutable;

/**
 * The {@code PartTag} class provides a separate ID type to delineate unique parts within a kit -
 * which may include multiple of the same type - which will be displayed to users when selecting parts.
 * Designed for use as a key in HashMaps as the primary front facing indexer for
 */
@Immutable
public class PartTag extends AbstractChecklist.Element<PartTag> {

    public static final PartTag NULL_TAG = new PartTag();
    public static final int CARDINALITY = (PartType.values().length - 1) * Byte.MAX_VALUE;

    public static final String COLLECTION_NAME = "PartTags";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String ORDINAL_FIELD_NAME = "ordinal";

    @NonNull private final PartType type;
    @NonNull private final Byte ordinal;


    private PartTag() {
        this.type = PartType.Null;
        this.ordinal = 0;
    }

    public PartTag(PartType type) {
        this(type, 1);
    }

    /**
     * @param type - the type of part being described in this tag.
     * @param ordinal - the ID relative to other parts of the same type in a kit.
     */
    public PartTag(PartType type, int ordinal) {

        if (type == PartType.Null) {
            throw new InvalidParameterException("Cannot instantiate null tag, must use constant.");
        }
        if (ordinal < 1 && type != PartType.Null) {
            throw new InvalidParameterException("Ordinal must be at least 1");
        }
        if (ordinal > Byte.MAX_VALUE) {
            throw new InvalidParameterException("Ordinal must be less than " + Byte.MAX_VALUE);
        }

        this.type = type;
        this.ordinal = (byte) ordinal;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected PartTag(Parcel in) {
        this.type = in.readTypedObject(PartType.CREATOR);
        this.ordinal = in.readByte();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(type, flags);
        dest.writeByte(ordinal);
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
    public boolean equals(PartTag other) {
        Boolean result;

        result = this.type.equals(other.type);
        result &= this.ordinal.equals(other.ordinal);

        return result;
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
    public PartTag clone() {
        return new PartTag(type, ordinal);
    }

    @Override
    public String toString() {
        return type.toString() + " " + ordinal.toString();
    }
}
