package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.TestConstants;
import com.example.afreecar.model.checklist.PartTag;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import static com.example.afreecar.model.TestConstants.*;

public class PartTest {

    private Part testPartConnInfo;

    private static final Terminal[] TERMINALS = new Terminal[] {CONTROLLER_TO_BATTERY_TERMINAL, CONTROLLER_TO_MOTOR_1_TERMINAL, CONTROLLER_TO_MOTOR_2_TERMINAL};
//    private static final boolean NEEDS_CHASSIS_CONN = true;
//
//    private static Terminal[] initializeTerminalInfos() {
//        return new Terminal[] {
//                new Terminal(new ID("1234"), new PartTag(PartType.Controller, 1)),
//                new Terminal(new ID("5678"), new PartTag(PartType.Battery, 1))
//        };
//    }

    @Before
    public void setUp() throws Exception {
        testPartConnInfo = TestConstants.CONTROLLER;
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testPartConnInfo.writeToParcel(parcel, testPartConnInfo.describeContents());
        parcel.setDataPosition(0);

        Part createdFromParcel = Part.CREATOR.createFromParcel(parcel);

        assertEquals(testPartConnInfo, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testPartConnInfo.describeContents());
    }

    @Test
    public void testGetTerminalIDsMap() {
        Map<PartTag, Terminal> expectedTerminalIDsMap = new HashMap<PartTag, Terminal>(TERMINALS.length);
        for (Terminal termInfo: TERMINALS) {
            expectedTerminalIDsMap.put(termInfo.getTarget(), termInfo);
        }

        assertEquals(expectedTerminalIDsMap, testPartConnInfo.getTerminalMap());
    }

    @Test
    public void testGetQRDistance() {
        assertEquals(STANDARD_QR_DISTANCE, testPartConnInfo.getQRDistance());
    }

    @Test
    public void testEquals() {
        assertEquals(new Part(CONTROLLER_ID, STANDARD_QR_DISTANCE, new HashSet<Terminal>(Arrays.asList(TERMINALS))), testPartConnInfo);
    }

    @Test
    public void testClone() {
        Part clone = testPartConnInfo.clone();
        assertEquals(testPartConnInfo, clone);
        assertNotSame(testPartConnInfo, clone);
    }
}