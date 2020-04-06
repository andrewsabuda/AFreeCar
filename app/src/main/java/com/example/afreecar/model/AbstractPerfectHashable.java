package com.example.afreecar.model;

public abstract class AbstractPerfectHashable<T extends AbstractPerfectHashable<T>> extends AbstractEquatable<T> implements PerfectHashable<T> {

    @Override
    public boolean equals(T other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(T other) {
        return this.hashCode() - other.hashCode();
    }

    // Breaks if you remove it
    @Override
    public abstract T clone();
}
