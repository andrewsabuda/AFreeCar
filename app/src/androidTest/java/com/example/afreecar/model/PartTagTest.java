package com.example.afreecar.model;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartTagTest {

    private PartTag testTag;
    private static final PartType firstType = PartType.Controller;
    private static final byte minOrdinal = 1;

    @Before
    public void setUp() throws Exception {
        testTag = new PartTag(firstType, minOrdinal);
    }

    @Test
    public void testGetType() {
        assertEquals(testTag.getType(), firstType);
    }

    @Test
    public void testGetOrdinal() {
        Assert.assertEquals((byte) testTag.getOrdinal(), minOrdinal);
    }

    @Test
    public void testEquals() {
        PartTag other = new PartTag(firstType, minOrdinal);
        assertEquals(testTag, other);

        other = new PartTag(firstType, 75);
        assertNotEquals(testTag, other);

        other = new PartTag(PartType.Battery, minOrdinal);
        assertNotEquals(testTag, other);
    }

    @Test
    public void testHashcode() {
        assertEquals(testTag.hashCode(), 0);

        testTag = new PartTag(firstType, Byte.MAX_VALUE);
        assertEquals(testTag.hashCode(), Byte.MAX_VALUE - 1);

        testTag = new PartTag(PartType.Battery, minOrdinal);
        assertEquals(testTag.hashCode(), Byte.MAX_VALUE);

        testTag = new PartTag(PartType.Battery, Byte.MAX_VALUE);
        assertEquals(testTag.hashCode(), (Byte.MAX_VALUE * 2) - 1);

        testTag = new PartTag(PartType.Motor, minOrdinal);
        assertEquals(testTag.hashCode(), Byte.MAX_VALUE * 2);

        testTag = new PartTag(PartType.Motor, Byte.MAX_VALUE);
        assertEquals(testTag.hashCode(), (Byte.MAX_VALUE * 3) - 1);
    }

    @Test
    public void testCompareTo() {
        PartTag other = new PartTag(firstType, minOrdinal);
        assertEquals(testTag.compareTo(other), 0);

        other = new PartTag(firstType, minOrdinal + 1);
        assertEquals(testTag.compareTo(other), -1);
        assertEquals(other.compareTo(testTag), 1);

        other = new PartTag(PartType.Battery, minOrdinal);
        assertEquals(testTag.compareTo(other), -Byte.MAX_VALUE);
        assertEquals(other.compareTo(testTag), Byte.MAX_VALUE);
    }

    @Test
    public void testToString() {
        String testString = "Controller 1";
        assertEquals(testTag.toString(), testString);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTag.writeToParcel(parcel,testTag.describeContents());
        parcel.setDataPosition(0);

        PartTag createdFromParcel = PartTag.CREATOR.createFromParcel(parcel);

        assertTrue(testTag.equals(createdFromParcel));
    }
}