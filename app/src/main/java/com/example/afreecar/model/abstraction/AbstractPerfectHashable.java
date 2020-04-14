package com.example.afreecar.model.abstraction;

import androidx.annotation.NonNull;

public abstract class AbstractPerfectHashable<TImpl extends AbstractPerfectHashable<TImpl>> extends AbstractHashable<TImpl> implements PerfectHashable<TImpl> {

    @Override
    public boolean equals(TImpl other) {
        Boolean result;

        result = super.equals(other);
        result &= this.hashCode() == other.hashCode();

        return result;
    }

    @Override
    public int compareTo(@NonNull TImpl other) {
        return this.hashCode() - other.hashCode();
    }

}
