package com.example.afreecar.model.abstraction;

public abstract class AbstractHashable<TImpl extends AbstractHashable<TImpl>> extends AbstractEquatable<TImpl> implements Hashable<TImpl> {

    @Override
    public abstract int hashCode();

}
