package com.example.afreecar.model.checklist;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.afreecar.model.PartType;
import com.example.afreecar.model.abstraction.AbstractPerfectHashable;

import java.security.InvalidParameterException;

/**
 * The {@code PartTag} class provides a separate ID type to delineate unique parts within a kit -
 * which may include multiple of the same type - which will be displayed to users when selecting parts.
 * Designed for use as a key in HashMaps as the primary front facing indexer for
 */
public class PartTag extends AbstractChecklist.Element<PartTag> {

    public static final String COLLECTION_NAME = "PartTags";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String ORDINAL_FIELD_NAME = "ordinal";

    private final PartType type;
    private final Byte ordinal;

    public static final int CARDINALITY = PartType.values().length * Byte.MAX_VALUE;

    /**
     * @param type - the type of part being described in this tag.
     * @param ordinal - the ID relative to other parts of the same type in a kit.
     */
    public PartTag(@NonNull PartType type, @NonNull int ordinal) {

        if (ordinal < 1) {
            throw new InvalidParameterException("Ordinal must be at least 1");
        }

        if (ordinal > Byte.MAX_VALUE) {
            throw new InvalidParameterException("Ordinal must be less than " + Byte.MAX_VALUE);
        }

        this.type = type;
        this.ordinal = (byte) ordinal;
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected PartTag(Parcel in) {
        this(in.readTypedObject(PartType.CREATOR), in.readByte());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(type, flags);
        dest.writeByte(ordinal);
    }

    public static final Creator<PartTag> CREATOR = new Creator<PartTag>() {
        @RequiresApi(api = Build.VERSION_CODES.M)
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
