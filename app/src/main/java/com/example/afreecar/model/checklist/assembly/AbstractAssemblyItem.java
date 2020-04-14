package com.example.afreecar.model.checklist.assembly;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.AbstractEquatable;

import java.util.Objects;

public abstract class AbstractAssemblyItem<TImpl extends AbstractAssemblyItem<TImpl>> extends AbstractQRDistance<TImpl> implements AssemblyItem<TImpl> {

    @NonNull private final ID id;

    public AbstractAssemblyItem(ID id, Double qrDistance) {
        super(qrDistance);
        this.id = id.clone();
    }

    private AbstractAssemblyItem(Double qrDistance, ID id) {
        super(qrDistance);
        this.id = id;
    }

    public AbstractAssemblyItem(Parcel in) {
        this(in.readDouble(), in.readTypedObject(ID.CREATOR));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedObject(id, flags);
    }

    @Override
    public ID getID() {
        return id.clone();
    }

    @Override
    public boolean equals(TImpl other) {
        Boolean result;

        result = super.equals(other);
        result &= this.id.equals(other.getID());

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.getQRDistance());
    }
}
