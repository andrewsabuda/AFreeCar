package com.example.afreecar.model.abstraction;

public abstract class AbstractHashable<T extends AbstractHashable<T>> extends AbstractEquatable<T> implements Hashable<T> {

    @Override
    public boolean equals(T other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(T other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public abstract int hashCode();

    // Breaks if you remove it
    @Override
    public abstract T clone();
}
