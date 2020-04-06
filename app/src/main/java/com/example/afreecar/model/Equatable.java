package com.example.afreecar.model;

public interface Equatable<T extends Equatable<T>> extends Cloneable{

    @Override
    boolean equals(Object other);

    boolean equals(T other);

    T clone();
}
