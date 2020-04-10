package com.example.afreecar.model.abstraction;

public interface Hashable<T extends Hashable<T>> extends Comparable<T>, Equatable<T> {

    @Override
    int hashCode();
}
