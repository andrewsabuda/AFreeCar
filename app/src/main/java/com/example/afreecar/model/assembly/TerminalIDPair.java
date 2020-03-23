package com.example.afreecar.model.assembly;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;

public class TerminalIDPair {

    private ID one;
    private ID two;

    public TerminalIDPair(@NonNull ID one, @NonNull ID two) {
        this.one = one;
        this.two = two;
    }

    public boolean verifyIDs(@NonNull ID one, @NonNull ID two) {
        return one.equals(one) && two.equals(two) || one.equals(two) && two.equals(one);
    }

    public boolean equals(Object other) {
        return other.getClass() == TerminalIDPair.class && this.equals((TerminalIDPair) other);
    }

    public boolean equals(TerminalIDPair other) {
        return this.verifyIDs(other.one, other.two);
    }
}
