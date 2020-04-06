package com.example.afreecar.model;

import android.os.Parcel;

import com.example.afreecar.model.PartTag;
import com.example.afreecar.model.PartTagPair;
import com.example.afreecar.model.PartType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartTagPairTest {

    private PartTagPair testPair;

    private static final PartTag ONE = new PartTag(PartType.Controller, 1);
    private static final PartTag TWO = new PartTag(PartType.Battery, 1);

    private static final PartTag ONE_UP = new PartTag(PartType.Controller, 2);
    private static final PartTag TWO_UP = new PartTag(PartType.Battery, 2);

    @Before
    public void setUp() {
        testPair = new PartTagPair(ONE, TWO);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testPair.writeToParcel(parcel, testPair.describeContents());
        parcel.setDataPosition(0);

        PartTagPair createdFromParcel = PartTagPair.CREATOR.createFromParcel(parcel);

        assertEquals(testPair, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testPair.describeContents());
    }

    @Test
    public void testHashCode() {
        assertEquals(Byte.MAX_VALUE, testPair.hashCode());
        assertEquals(TWO.hashCode(), testPair.hashCode());

        // Two is the little end
        assertEquals(Byte.MAX_VALUE + 1, new PartTagPair(ONE, TWO_UP).hashCode());

        // One is the big end
        assertEquals(PartTag.CARDINALITY + Byte.MAX_VALUE, new PartTagPair(ONE_UP, TWO).hashCode());
    }

    @Test
    public void testEquals() {
        assertEquals(testPair, testPair.clone());
        assertNotEquals(testPair, new PartTagPair(ONE, TWO_UP));
    }

    @Test
    public void testClone() {
        PartTagPair clone = testPair.clone();
        assertEquals(testPair, clone);
        assertNotSame(testPair, clone);
    }

    @Test
    public void testCompareTo() {
        assertEquals(-1, testPair.compareTo(new PartTagPair(ONE, TWO_UP)));
        assertEquals(-PartTag.CARDINALITY, testPair.compareTo(new PartTagPair(ONE_UP, TWO)));
    }

    @Test
    public void testToString() {
        assertEquals("Controller 1 - Battery 1", testPair.toString());
    }
}
