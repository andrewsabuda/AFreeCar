package com.example.afreecar.model.checklist;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.RequiresApi;

/**
 * Entity indicating a required connection between two unique parts.
 */
public class PartTagPair extends AbstractChecklistElement<PartTagPair> {

    private final PartTag one;
    private final PartTag two;

    private static int CARDINALITY = ((Double) Math.pow(PartTag.CARDINALITY, 2)).intValue();

    public PartTagPair(PartTag one, PartTag two) {
        super(true);

        // Ensures sorting of parts internally
        int comparison = one.compareTo(two);
        if (comparison == 0) {
            throw new IllegalArgumentException("Cannot connect a part to itself.");
        }
        else if (comparison < 0) {
            this.one = one;
            this.two = two;
        }
        else {
            this.one = two;
            this.two = one;
        }
    }


    // BEGIN PARCELABLE IMPLEMENTATION

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected PartTagPair(Parcel in) {
        this(in.readTypedObject(PartTag.CREATOR), in.readTypedObject(PartTag.CREATOR));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(one, flags);
        dest.writeTypedObject(two, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PartTagPair> CREATOR = new Creator<PartTagPair>() {
        @Override
        public PartTagPair createFromParcel(Parcel in) {
            return new PartTagPair(in);
        }

        @Override
        public PartTagPair[] newArray(int size) {
            return new PartTagPair[size];
        }
    };

    // END PARCELABLE IMPLEMENTATION

    @Override
    public int hashCode() {
        return (one.hashCode() * PartTag.CARDINALITY) + two.hashCode();
    }

    @Override
    public PartTagPair clone() {
        return new PartTagPair(one.clone(), two.clone());
    }

    @Override
    public String toString() {
        return one.toString() + " - " + two.toString();
    }
}
