package com.example.afreecar.model.checklist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

public class CheckList implements Parcelable {

    private AbstractChecklistElement[] checkList;

    public CheckList(Set<AbstractChecklistElement> elements) {
        checkList = elements.toArray(new AbstractChecklistElement[elements.size()]);
    }

    // BEGIN PARCELABLE IMPLEMENTATION

    protected CheckList(Parcel in) {
        checkList = in.createTypedArray(AbstractChecklistElement.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(checkList, flags);
    }

    public static final Creator<CheckList> CREATOR = new Creator<CheckList>() {
        @Override
        public CheckList createFromParcel(Parcel in) {
            return new CheckList(in);
        }

        @Override
        public CheckList[] newArray(int size) {
            return new CheckList[size];
        }
    };

    // BEGIN PARCELABLE IMPLEMENTATION

    public String[] display() {
        String[] displayList = new String[checkList.length];

        for (int i = 0; i < checkList.length; i++) {
            displayList[i] = checkList[i].toString();
        }

        return displayList;
    }
}
