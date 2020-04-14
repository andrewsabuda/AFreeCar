package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.abstraction.Hashable;

import java.util.Objects;

import javax.annotation.concurrent.Immutable;

/**
 * Class describing the intended distance between either the
 */
@Immutable
public class QRCodePair extends AbstractQRDistance<QRCodePair> implements Hashable<QRCodePair>, Parcelable {

    @NonNull private final ID one, two;

    private QRCodePair(@NonNull Double qrDistance, @NonNull ID one, @NonNull ID two) {
        super(qrDistance);

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
    }

    public QRCodePair(@NonNull Terminal terminalOne, @NonNull Terminal terminalTwo) {
        this(terminalOne.getQRDistance() + terminalTwo.getQRDistance(), terminalOne.getID(), terminalTwo.getID());
    }

    public QRCodePair(@NonNull Part part) {
        this(part.getQRDistance(), part.getID(), ID.CHASSIS);
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected QRCodePair(@NonNull Parcel in) {
        this(in.readDouble(), in.readTypedObject(ID.CREATOR), in.readTypedObject(ID.CREATOR));
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, @NonNull int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedObject(one, flags);
        dest.writeTypedObject(two, flags);
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
        return Objects.hash(this.one, this.two, this.getQRDistance());
    }

    @Override
    public QRCodePair clone() {
        return new QRCodePair(this.getQRDistance(), one, two);
    }

    @Override
    public boolean equals(QRCodePair other) {
        Boolean result;
        result = super.equals(other);
        result &= this.one.equals(other.one);
        result &= this.two.equals(other.two);
        return result;
    }
}
