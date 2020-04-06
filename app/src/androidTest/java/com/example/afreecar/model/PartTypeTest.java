package com.example.afreecar.model;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartTypeTest {

    private PartType testPartType;

    @Before
    public void setUp() {
        testPartType = PartType.Controller;
    }

    @Test
    public void testEquals() {
        PartType otherPartType = PartType.Controller;

        Assert.assertEquals(testPartType, otherPartType);

        otherPartType = PartType.Motor;
        assertNotEquals(testPartType, otherPartType);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testPartType.writeToParcel(parcel, testPartType.describeContents());
        parcel.setDataPosition(0);

        PartType createdFromParcel = PartType.CREATOR.createFromParcel(parcel);

        assertTrue(testPartType.equals(createdFromParcel));
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testPartType.describeContents());
    }
}
