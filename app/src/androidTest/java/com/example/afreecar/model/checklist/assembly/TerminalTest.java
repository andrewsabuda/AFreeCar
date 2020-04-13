package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import static com.example.afreecar.model.TestConstants.*;

import static org.junit.Assert.*;

public class TerminalTest {

    private Terminal testInfo;

    @Before
    public void setUp() {
        testInfo = CONTROLLER_TO_BATTERY_TERMINAL;
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testInfo.describeContents());
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testInfo.writeToParcel(parcel, testInfo.describeContents());
        parcel.setDataPosition(0);

        Terminal createdFromParcel = Terminal.CREATOR.createFromParcel(parcel);

        assertEquals(testInfo, createdFromParcel);
    }

    @Test
    public void testGetID() {
        assertEquals(CONTROLLER_TO_BATTERY_TERMINAL_ID, testInfo.getID());
    }

    @Test
    public void testGetTarget() {
        assertEquals(BATTERY_TAG, testInfo.getTarget());
    }

    @Test
    public void testEquals() {
        assertEquals(new Terminal(CONTROLLER_TO_BATTERY_TERMINAL_ID, STANDARD_QR_DISTANCE, BATTERY_TAG), testInfo);
        assertNotEquals(CONTROLLER_TO_MOTOR_1_TERMINAL, testInfo);
        assertNotEquals(CONTROLLER_TO_MOTOR_2_TERMINAL, testInfo);
    }

    @Test
    public void testClone() {
        Terminal clone = testInfo.clone();
        assertEquals(testInfo, clone);
        assertNotSame(testInfo, clone);
    }
}