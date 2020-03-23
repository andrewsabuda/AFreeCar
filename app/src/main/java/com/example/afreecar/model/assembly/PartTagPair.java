package com.example.afreecar.model.assembly;

import com.example.afreecar.model.PartTag;

/**
 * Entity indicating a required connection between two unique parts.
 */
public class PartTagPair implements Comparable<PartTagPair> {

    private PartTag one;
    private PartTag two;

    public PartTagPair(PartTag one, PartTag two) {
        // Ensures sorting of parts internally
        int comparison = one.compareTo(two);
        if (comparison == 0) {
            throw new IllegalArgumentException("Cannot connect a part to itself.");
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

    @Override
    public int hashCode() {
        return (one.hashCode() * PartTag.Cardinality) + two.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == PartTagPair.class && this.equals((PartTagPair) other);
    }

    public boolean equals(PartTagPair other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(PartTagPair other) {
        return this.hashCode() - other.hashCode();
    }
}
