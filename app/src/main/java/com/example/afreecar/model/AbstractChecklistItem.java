package com.example.afreecar.model;

import android.os.Parcelable;

public abstract class AbstractChecklistItem implements Parcelable {

    private boolean isPair;

    public AbstractChecklistItem(boolean isPair) {
        this.isPair = isPair;
    }

    public boolean isPair() {
        return isPair;
    }

    @Override
    public abstract String toString();
}
