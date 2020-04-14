package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.abstraction.AbstractEquatable;

public abstract class AbstractQRDistance<TImpl extends AbstractQRDistance<TImpl>> extends AbstractEquatable<TImpl> implements QRDistance<TImpl>, Parcelable {

    @NonNull private final Double qrDistance;

    public AbstractQRDistance(Double qrDistance) {
        this.qrDistance = qrDistance;
    }

    @Override
    public Double getQRDistance() {
        return Double.valueOf(qrDistance);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(qrDistance);
    }

    @Override
    public boolean equals(TImpl other) {
        Boolean result;
        result = super.equals(other);
        result &= this.qrDistance.equals(other.getQRDistance());
        return result;
    }
}
