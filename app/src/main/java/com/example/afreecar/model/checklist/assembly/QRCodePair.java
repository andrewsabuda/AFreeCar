package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afreecar.model.abstraction.AbstractHashable;
import com.example.afreecar.model.ID;

import java.util.Objects;

/**
 * Class describing the intended distance between either the
 */
public class QRCodePair extends AbstractHashable<QRCodePair> implements Parcelable {

    @NonNull private final ID one, two;
    @NonNull private final Double qrDistance;

    private QRCodePair(@NonNull ID one, @NonNull ID two, Double qrDistance) {
        // Ensures sorting of parts internally
        int comparison = one.compareTo(two);
        if (comparison == 0) {
            throw new IllegalArgumentException("Cannot pair a QR code to itself.");
        }
        else if (comparison < 0) {
            this.one = one.clone();
            this.two = two.clone();
        }
        else {
            this.one = two.clone();
            this.two = one.clone();
        }

        this.qrDistance = Double.valueOf(qrDistance);
    }

    public QRCodePair(@NonNull Terminal terminalOne, @NonNull Terminal terminalTwo) {
        this(terminalOne.getID(), terminalTwo.getID(),terminalOne.getQRDistance() + terminalTwo.getQRDistance());
    }

    public QRCodePair(@NonNull Part part) {
        this(part.getID(), ID.CHASSIS, part.getQRDistance());
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected QRCodePair(@NonNull Parcel in) {
        this(in.readTypedObject(ID.CREATOR), in.readTypedObject(ID.CREATOR), in.readDouble());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, @NonNull int flags) {
        dest.writeTypedObject(one, flags);
        dest.writeTypedObject(two, flags);
        dest.writeDouble(qrDistance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QRCodePair> CREATOR = new Creator<QRCodePair>() {
        @Override
        public QRCodePair createFromParcel(@NonNull Parcel in) {
            return new QRCodePair(in);
        }

        @Override
        public QRCodePair[] newArray(@NonNull int size) {
            return new QRCodePair[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    @Override
    public int hashCode() {
        return Objects.hash(one, two, qrDistance);
    }

    @Override
    public QRCodePair clone() {
        return new QRCodePair(one, two, qrDistance);
    }

    @Override
    public boolean equals(QRCodePair other) {
        return this.one.equals(other.one) && this.two.equals(other.two);
    }
}
