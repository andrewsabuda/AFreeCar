package com.example.afreecar.model.abstraction;

public abstract class AbstractEquatable<T extends AbstractEquatable<T>> implements Equatable<T> {

    @Override
    public boolean equals(Object other) {
        return this == null
                ? other == null
                : other.getClass() == getClass()
                    ? this.equals((T) other)
                    : this == other;
    }

    // Breaks if you remove it
    @Override
    public abstract T clone();
}
