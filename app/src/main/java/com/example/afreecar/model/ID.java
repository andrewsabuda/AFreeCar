package com.example.afreecar.model;

import androidx.annotation.NonNull;

/**
 * Wrapper class for IDs to be read from QR codes, in case we use something besides {@code String}s.
 */
public class ID {

    private String id;

    public ID(@NonNull String id) {
        this.id = id;
    }

    public ID Copy() {
        return new ID(this.id);
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == ID.class && this.equals((ID) other);
    }

    public boolean equals(ID other) {
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
