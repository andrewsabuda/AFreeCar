package com.example.afreecar.model;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartTagTest {

    private PartTag testTag;

    private static final PartType FIRST_TYPE = PartType.Controller;
    private static final byte MIN_ORDINAL = 1;

    @Before
    public void setUp() throws Exception {
        testTag = new PartTag(FIRST_TYPE, MIN_ORDINAL);
    }

    @Test
    public void testGetType() {
        assertEquals(testTag.getType(), FIRST_TYPE);
    }

    @Test
    public void testGetOrdinal() {
        Assert.assertEquals((byte) testTag.getOrdinal(), MIN_ORDINAL);
    }

    @Test
    public void testEquals() {
        PartTag expected = new PartTag(FIRST_TYPE, MIN_ORDINAL);
        assertEquals(expected, testTag);

        expected = new PartTag(FIRST_TYPE, MIN_ORDINAL + 1);
        assertNotEquals(expected, testTag);

        expected = new PartTag(PartType.Battery, MIN_ORDINAL);
        assertNotEquals(expected, testTag);
    }

    @Test
    public void testHashcode() {
        assertEquals(0, testTag.hashCode());

        testTag = new PartTag(FIRST_TYPE, Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE - 1, testTag.hashCode());

        testTag = new PartTag(PartType.Battery, MIN_ORDINAL);
        assertEquals(Byte.MAX_VALUE, testTag.hashCode());

        testTag = new PartTag(PartType.Battery, Byte.MAX_VALUE);
        assertEquals((Byte.MAX_VALUE * 2) - 1, testTag.hashCode());

        testTag = new PartTag(PartType.Motor, MIN_ORDINAL);
        assertEquals(Byte.MAX_VALUE * 2, testTag.hashCode());

        // Highest value
        testTag = new PartTag(PartType.Motor, Byte.MAX_VALUE);
        assertEquals(testTag.hashCode(), (Byte.MAX_VALUE * 3) - 1);
    }

    @Test
    public void testCompareTo() {
        PartTag other = new PartTag(FIRST_TYPE, MIN_ORDINAL);
        assertEquals(0, testTag.compareTo(other));

        other = new PartTag(FIRST_TYPE, MIN_ORDINAL + 1);
        assertEquals(-1, testTag.compareTo(other));
        assertEquals(1, other.compareTo(testTag));

        other = new PartTag(PartType.Battery, MIN_ORDINAL);
        assertEquals(-Byte.MAX_VALUE, testTag.compareTo(other));
        assertEquals(Byte.MAX_VALUE, other.compareTo(testTag));
    }

    @Test
    public void testToString() {
        assertEquals("Controller 1", testTag.toString());
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTag.writeToParcel(parcel,testTag.describeContents());
        parcel.setDataPosition(0);

        PartTag createdFromParcel = PartTag.CREATOR.createFromParcel(parcel);

        assertEquals(testTag, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testTag.describeContents());
    }
}
