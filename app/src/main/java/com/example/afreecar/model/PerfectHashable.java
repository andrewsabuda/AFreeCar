package com.example.afreecar.model;

public interface PerfectHashable<T extends PerfectHashable<T>> extends Comparable<T>, Equatable<T> {

    @Override
    int hashCode();
}
