package com.example.afreecar.model;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;

/**
 * The {@code PartTag} class provides a separate ID type to delineate unique parts within a kit -
 * which may include multiple of the same type - which will be displayed to users when selecting parts.
 * Designed for use as a key in HashMaps as the primary front facing indexer for
 */
public class PartTag implements Comparable<PartTag>{

    private PartType type;
    private Byte number;

    public static final int Cardinality = PartType.Cardinality * Byte.MAX_VALUE;

    /**
     * @param type - the type of part being described in this tag.
     * @param number - the ID relative to other parts of the same type in a kit.
     */
    public PartTag(@NonNull PartType type, @NonNull int number) {
        if (number < 1) {
            throw new InvalidParameterException("Number must be at least 1");
        }

        if (number > Byte.MAX_VALUE) {
            throw new InvalidParameterException("Number must be less than " + Byte.MAX_VALUE);
        }

        this.type = type;
        this.number = (byte) number;
    }

    public PartType getType() {
        return type;
    }

    public Byte getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == PartType.class && this.equals((PartTag) other);
    }

    public boolean equals(PartTag other) {
        return type == other.type && number == other.number;
    }

    @Override
    public int hashCode() {
        return (type.ordinal() * Byte.MAX_VALUE) + number - 1;
    }

    @Override
    public int compareTo(@NonNull PartTag other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        return type.toString() + " " + number.toString();
    }
}
