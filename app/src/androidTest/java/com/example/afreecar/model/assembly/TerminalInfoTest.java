package com.example.afreecar.model.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;
import com.example.afreecar.model.PartType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalInfoTest {

    private TerminalInfo testInfo;

    private static final ID TERMINAL_ID = new ID("1234");
    private static final PartTag TARGET_TAG = new PartTag(PartType.Controller, 1);

    @Before
    public void setUp() {
        testInfo = new TerminalInfo(TERMINAL_ID, TARGET_TAG);
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

        TerminalInfo createdFromParcel = TerminalInfo.CREATOR.createFromParcel(parcel);

        assertEquals(testInfo, createdFromParcel);
    }

    @Test
    public void testGetID() {
        assertEquals(TERMINAL_ID, testInfo.getID());
    }

    @Test
    public void testGetTarget() {
        assertEquals(TARGET_TAG, testInfo.getTarget());
    }

    @Test
    public void testEquals() {
        assertEquals(new TerminalInfo(TERMINAL_ID, TARGET_TAG), testInfo);
        assertNotEquals(new TerminalInfo(new ID("5678"), TARGET_TAG), testInfo);
        assertNotEquals(new TerminalInfo(TERMINAL_ID, new PartTag(PartType.Controller, 2)), testInfo);
    }

    @Test
    public void testClone() {
        TerminalInfo clone = testInfo.clone();
        assertEquals(testInfo, clone);
        assertNotSame(testInfo, clone);
    }
}