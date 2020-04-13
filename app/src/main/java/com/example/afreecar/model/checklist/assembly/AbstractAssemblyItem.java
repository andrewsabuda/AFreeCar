package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.AbstractEquatable;

import java.util.Objects;

public abstract class AbstractAssemblyItem<T extends AbstractAssemblyItem<T>> extends AbstractEquatable<T> implements AssemblyItem<T>, Parcelable {

    @NonNull private final ID id;
    @NonNull private final Double qrDistance;

    public AbstractAssemblyItem(ID id, Double qrDistance) {
        this.id = id.clone();
        this.qrDistance = Double.valueOf(qrDistance);
    }

    public AbstractAssemblyItem(Parcel in) {
        this(in.readTypedObject(ID.CREATOR), in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(id, flags);
        dest.writeDouble(qrDistance);
    }

    @Override
    public ID getID() {
        return id.clone();
    }

    @Override
    public Double getQRDistance() {
        return Double.valueOf(qrDistance);
    }

    @Override
    public boolean equals(T other) {
        return this.id.equals(other.getID()) && other.getQRDistance().equals(other.getQRDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qrDistance);
    }
}
