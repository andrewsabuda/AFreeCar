package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalIDPairTest {

    private TerminalIDPair testTermIDPair;

    private static final ID TERMINAL_ID_ONE = new ID("1234");
    private static final ID TERMINAL_ID_TWO = new ID("5678");

    @Before
    public void setUp() {
        testTermIDPair = new TerminalIDPair(TERMINAL_ID_ONE, TERMINAL_ID_TWO);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTermIDPair.writeToParcel(parcel, testTermIDPair.describeContents());
        parcel.setDataPosition(0);

        TerminalIDPair createdFromParcel = TerminalIDPair.CREATOR.createFromParcel(parcel);

        assertEquals(testTermIDPair, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testTermIDPair.describeContents());
    }

    @Test
    public void testEquals() {
        assertEquals(new TerminalIDPair(TERMINAL_ID_ONE, TERMINAL_ID_TWO), testTermIDPair);
    }

    @Test
    public void testClone() {
        TerminalIDPair clone = testTermIDPair.clone();
        assertEquals(testTermIDPair, clone);
        assertNotSame(testTermIDPair, clone);
    }
}
