package com.example.afreecar.model.checklist;

import android.os.Parcel;

/**
 * Entity indicating a required connection between two unique parts.
 */
public class PartTagPair extends AbstractChecklistElement<PartTagPair> {

    private PartTag one;
    private PartTag two;

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

    protected PartTagPair(Parcel in) {
        this(
                (PartTag) in.readParcelable(PartTag.class.getClassLoader()),
                (PartTag) in.readParcelable(PartTag.class.getClassLoader())
        );
//        PartTag[] tags = new PartTag[2];
//        in.readTypedArray(tags, PartTag.CREATOR);
//
//        this.one = tags[0];
//        this.two = tags[1];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(one, flags);
        dest.writeParcelable(two, flags);
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
