package com.example.afreecar.model.abstraction;

public abstract class AbstractPerfectHashable<TImpl extends AbstractPerfectHashable<TImpl>> extends AbstractHashable<TImpl> implements PerfectHashable<TImpl> {

    @Override
    public boolean equals(TImpl other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(TImpl other) {
        return this.hashCode() - other.hashCode();
    }

    // Breaks if you remove it
    @Override
    public abstract TImpl clone();

}
