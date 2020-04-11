package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.abstraction.AbstractHashable;

import java.util.Objects;

public abstract class AbstractAssemblyItem<T extends AbstractAssemblyItem<T>> extends AbstractHashable<T> implements AssemblyItem<T>, Parcelable {

    private final ID id;
    private final Double qrDistance;

    public AbstractAssemblyItem(ID id, Double qrDistance) {
        this.id = id.clone();
        this.qrDistance = Double.valueOf(qrDistance);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public AbstractAssemblyItem(Parcel in) {
        this(in.readTypedObject(ID.CREATOR), in.readDouble());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, qrDistance);
    }
}
