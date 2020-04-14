package com.example.afreecar.model.abstraction;

public abstract class AbstractEquatable<TImpl extends AbstractEquatable<TImpl>> implements Equatable<TImpl> {

    @Override
    public boolean equals(Object other) {
        return this == null
                ? other == null
                : other.getClass() == getClass()
                    ? this.equals((TImpl) other)
                    : this == other;
    }

    @Override
    public boolean equals(TImpl other) {
        return this == null
                ? other == null
                : other != null;
    }

    // Breaks if you remove it
    @Override
    public abstract TImpl clone();
}
