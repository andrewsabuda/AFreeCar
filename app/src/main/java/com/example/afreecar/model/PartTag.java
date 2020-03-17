package com.example.afreecar.model;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;

/**
 * The {@code PartTag} class provides a separate ID type to delineate unique parts within a kit -
 * which may include multiple of the same type - which will be displayed to users when selecting parts.
 */
public class PartTag {

    private PartType type;
    private Byte number;

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

    @Override
    public String toString() {
        return type.toString() + " " + number.toString();
    }
}
