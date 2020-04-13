package com.example.afreecar.model.abstraction;

public interface Equatable<TImpl extends Equatable<TImpl>> extends Cloneable {

    @Override
    boolean equals(Object other);

    boolean equals(TImpl other);

    TImpl clone();

}
