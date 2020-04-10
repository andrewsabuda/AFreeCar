package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.PartType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalInfoTest {

    private Terminal testInfo;

    private static final ID TERMINAL_ID = new ID("1234");
    private static final PartTag TARGET_TAG = new PartTag(PartType.Controller, 1);

    @Before
    public void setUp() {
        testInfo = new Terminal(TERMINAL_ID, TARGET_TAG);
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
        assertEquals(TERMINAL_ID, testInfo.getID());
    }

    @Test
    public void testGetTarget() {
        assertEquals(TARGET_TAG, testInfo.getTarget());
    }

    @Test
    public void testEquals() {
        assertEquals(new Terminal(TERMINAL_ID, TARGET_TAG), testInfo);
        assertNotEquals(new Terminal(new ID("5678"), TARGET_TAG), testInfo);
        assertNotEquals(new Terminal(TERMINAL_ID, new PartTag(PartType.Controller, 2)), testInfo);
    }

    @Test
    public void testClone() {
        Terminal clone = testInfo.clone();
        assertEquals(testInfo, clone);
        assertNotSame(testInfo, clone);
    }
}