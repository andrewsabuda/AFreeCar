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

    public boolean equals(ID other) {
        return this.id.equals(other.id);
    }

    @Override
    public java.lang.String toString() {
        return id;
    }
}
