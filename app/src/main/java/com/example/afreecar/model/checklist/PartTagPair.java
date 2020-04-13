package com.example.afreecar.model.checklist;

import android.os.Build;
import android.os.Parcel;

import androidx.annotation.RequiresApi;

import com.example.afreecar.model.abstraction.Equatable;
import com.example.afreecar.model.checklist.assembly.Part;

/**
 * Entity indicating a required connection between two unique parts.
 */
public class PartTagPair extends AbstractChecklist.PairableElement<PartTagPair, PartTag> {

    private static int CARDINALITY = ((Double) Math.pow(PartTag.CARDINALITY, 2)).intValue();

    public PartTagPair(PartTag one, PartTag two) {
        super(one, two);
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
        @RequiresApi(api = Build.VERSION_CODES.M)
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
