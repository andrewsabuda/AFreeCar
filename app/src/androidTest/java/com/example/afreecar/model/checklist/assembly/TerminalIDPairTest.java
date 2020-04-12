package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static com.example.afreecar.model.TestConstants.*;

public class TerminalIDPairTest {

    private TerminalPair testTermIDPair;

    private static final ID TERMINAL_ID_ONE = new ID("1234");
    private static final ID TERMINAL_ID_TWO = new ID("5678");

    @Before
    public void setUp() {
        testTermIDPair = new TerminalPair(CONTROLLER_TO_BATTERY_TERMINAL, BATTERY_TO_CONTROLLER_TERMINAL);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTermIDPair.writeToParcel(parcel, testTermIDPair.describeContents());
        parcel.setDataPosition(0);

        TerminalPair createdFromParcel = TerminalPair.CREATOR.createFromParcel(parcel);

        assertEquals(testTermIDPair, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testTermIDPair.describeContents());
    }

    @Test
    public void testEquals() {
        assertEquals(new TerminalPair(BATTERY_TO_CONTROLLER_TERMINAL, CONTROLLER_TO_BATTERY_TERMINAL), testTermIDPair);
    }

    @Test
    public void testClone() {
        TerminalPair clone = testTermIDPair.clone();
        assertEquals(testTermIDPair, clone);
        assertNotSame(testTermIDPair, clone);
    }
}
