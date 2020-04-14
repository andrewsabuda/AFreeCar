package com.example.afreecar.model.checklist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afreecar.model.OptionalPair;
import com.example.afreecar.model.abstraction.AbstractEquatable;
import com.example.afreecar.model.abstraction.AbstractPerfectHashable;
import com.example.afreecar.model.abstraction.Equatable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractChecklist<
            TImpl extends AbstractChecklist<TImpl, TElement, TFulfill>,
            TElement extends AbstractChecklist.Element<TElement>,
            TFulfill extends Equatable<TFulfill>>
        extends AbstractEquatable<TImpl> implements Checklist<TImpl, TElement, AbstractChecklist.Element<TElement>.DisplayImpl, TFulfill>
{
    @Override
    public Element<TElement>.DisplayImpl getDisplay(TElement element) {
        TElement.DisplayImpl display;

        Boolean isFulfilled = this.isFulfilled(element);
        display = element.toDisplay(isFulfilled);

        return display;
    }

    @Override
    public List<Element<TElement>.DisplayImpl> getDisplays() {
        ArrayList<Element<TElement>.DisplayImpl> displaysList;

        List<TElement> elementsList = getElements();
        displaysList = new ArrayList<Element<TElement>.DisplayImpl>(elementsList.size());

        for (TElement element: this.getElements()) {
            displaysList.add(getDisplay(element));
        }

        return displaysList;
    }

    @Override
    public Boolean isChecklistFulfilled() {
        Boolean result = true;

        for (TElement element: this.getElements()) {
            result &= this.isFulfilled(element);
        }

        return result;
    }

    @Override
    public abstract TImpl clone();

    public static abstract class Element<TElement extends AbstractChecklist.Element<TElement>> extends AbstractPerfectHashable<TElement> implements Checklist.Element<TElement, Element<TElement>.DisplayImpl>, Parcelable {

        // Breaks if you remove it
        @Override
        public abstract TElement clone();

        @Override
        public abstract String toString();

        @Override
        public TElement.DisplayImpl toDisplay(Boolean isFulfilled) {
            return new TElement.DisplayImpl((TElement) this, isFulfilled);
        }

        // BEGIN PARCELABLE IMPLEMENTATION

        @Override
        public int describeContents() {
            return 0;
        }

        // END PARCELABLE IMPLEMENTATION

        public class DisplayImpl extends AbstractPerfectHashable<DisplayImpl> implements Checklist.Element.Display<Element<TElement>.DisplayImpl, TElement> {

            @NonNull final TElement source;
            @NonNull final Boolean isFulfilled;

            public DisplayImpl(TElement source) {
                this(source, false);
            }

            public DisplayImpl(TElement source, Boolean isFulfilled) {
                this.source = source;
                this.isFulfilled = isFulfilled;
            }

            @Override
            public TElement getSource() {
                return source;
            }

            @Override
            public Boolean isFulfilled() {
                return isFulfilled;
            }

            @Override
            public boolean equals(DisplayImpl other) {
                Boolean result;

                result = this.source.equals(other.source);
                result &= this.isFulfilled == other.isFulfilled;

                return result;
            }

            @Override
            public int hashCode() {
                int hash;
                hash = Objects.hash(source, isFulfilled);
                return hash;
            }

            @Override
            public DisplayImpl clone() {
                return new DisplayImpl(source, isFulfilled);
            }
        }
    }



    public static abstract class PairableElement<TPair extends PairableElement<TPair, TSingle>, TSingle extends Element<TSingle>> extends Element<TPair> implements OptionalPair<TSingle> {

        @NonNull final TSingle one;
        @NonNull final TSingle two;

        public PairableElement(TSingle one, TSingle two) {

            // Ensures internal sorting of paired elements
            int comparison = one.compareTo(two);
            if (comparison == 0) {
                throw new IllegalArgumentException("Cannot pair an item with itself.");
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

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedObject(one, flags);
            dest.writeTypedObject(two, flags);
        }

        // END PARCELABLE IMPLEMENTATION

        @Override
        public boolean equals(TPair other) {
            Boolean result;

            result = this.one.equals(other.one);
            result &= this.two.equals(other.two);

            return result;
        }

        @Override
        public int hashCode() {
            int hash;

            hash = Objects.hash(one, two);

            return hash;
        }

        @Override
        public Boolean isPair() {
            return this.two != null;
        }

        @Override
        public TSingle getOne() {
            return this.one;
        }

        public TSingle getTwo() {
            return this.two;
        }
    }
}
