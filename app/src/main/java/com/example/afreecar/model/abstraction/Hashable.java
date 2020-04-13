package com.example.afreecar.model.abstraction;

public interface Hashable<TImpl extends Hashable<TImpl>> extends Equatable<TImpl> {

    @Override
    int hashCode();

}
