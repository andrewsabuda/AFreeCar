package com.example.afreecar.model.assembly;

import android.os.Parcel;

import com.example.afreecar.model.AbstractChecklistItem;
import com.example.afreecar.model.PartTag;

/**
 * Entity indicating a required connection between two unique parts.
 */
public class PartTagPair extends AbstractChecklistItem implements Comparable<PartTagPair> {

    private PartTag one;
    private PartTag two;

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
        super(true);
        PartTag[] tags = new PartTag[2];
        in.readTypedArray(tags, PartTag.CREATOR);

        this.one = tags[0];
        this.two = tags[1];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(new PartTag[] {one, two}, flags);
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
        return (one.hashCode() * PartTag.Cardinality) + two.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == PartTagPair.class && this.equals((PartTagPair) other);
    }

    public boolean equals(PartTagPair other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(PartTagPair other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        return null;
    }
}
