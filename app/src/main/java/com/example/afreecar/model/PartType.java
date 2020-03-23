package com.example.afreecar.model;

/**
 * Enumeration describing all possible types of parts that may be found in an eKit, as well as
 * an entry for the chassis itself for use in modeling physical connections to parts.
 */
public enum PartType {
    Controller,
    Battery,
    Motor;

    public static final int Cardinality = PartType.values().length;
}
