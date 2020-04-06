package com.example.afreecar.model;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IDTest {

    private ID testID;

    private static final String ID_STRING = "01234";

    @Before
    public void setUp() {
        testID = new ID(ID_STRING);
    }

    @Test
    public void testEquals() {
        assertEquals(testID, new ID(ID_STRING));

        assertNotEquals(testID, new ID(ID_STRING + "5"));

        // Test equals(object)
        assertNotEquals(testID, ID_STRING);
    }

    @Test
    public void testClone() {
        ID clone = testID.clone();
        assertEquals(testID, clone);

        // Asserts that they are not a pointer to the same object
        assertNotSame(testID, clone);
    }

    @Test
    public void testHashCode() {
        assertEquals(testID.hashCode(), testID.toString().hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(testID.toString(), ID_STRING);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testID.writeToParcel(parcel, testID.describeContents());
        parcel.setDataPosition(0);

        ID createdFromParcel = ID.CREATOR.createFromParcel(parcel);

        assertEquals(testID, createdFromParcel);
    }
}
