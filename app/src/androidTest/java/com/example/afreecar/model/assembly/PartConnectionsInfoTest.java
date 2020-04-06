package com.example.afreecar.model.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;
import com.example.afreecar.model.PartType;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class PartConnectionsInfoTest {

    private PartConnectionsInfo testPartConnInfo;

    private static final TerminalInfo[] TERMINAL_INFOS = initializeTerminalInfos();
    private static final boolean NEEDS_CHASSIS_CONN = true;

    private static TerminalInfo[] initializeTerminalInfos() {
        return new TerminalInfo[] {
                new TerminalInfo(new ID("1234"), new PartTag(PartType.Controller, 1)),
                new TerminalInfo(new ID("5678"), new PartTag(PartType.Battery, 1))
        };
    }

    @Before
    public void setUp() throws Exception {
        testPartConnInfo = new PartConnectionsInfo(NEEDS_CHASSIS_CONN, new HashSet<TerminalInfo>(Arrays.asList(TERMINAL_INFOS)));
    }

    @Test
    public void writeToParcel() {
        Parcel parcel = Parcel.obtain();
        testPartConnInfo.writeToParcel(parcel, testPartConnInfo.describeContents());
        parcel.setDataPosition(0);

        PartConnectionsInfo createdFromParcel = PartConnectionsInfo.CREATOR.createFromParcel(parcel);

        assertEquals(testPartConnInfo, createdFromParcel);
    }

    @Test
    public void describeContents() {
        assertEquals(0, testPartConnInfo.describeContents());
    }

    @Test
    public void getTerminalIDsMap() {
        Map<PartTag, ID> expectedTerminalIDsMap = new HashMap<PartTag, ID>(TERMINAL_INFOS.length);
        for (TerminalInfo termInfo: TERMINAL_INFOS) {
            expectedTerminalIDsMap.put(termInfo.getTarget(), termInfo.getID());
        }

        assertEquals(expectedTerminalIDsMap, testPartConnInfo.getTerminalIDsMap());
    }

    @Test
    public void getChassisConnectionRequirement() {
        assertEquals(NEEDS_CHASSIS_CONN, testPartConnInfo.getChassisConnectionRequirement());
    }

    @Test
    public void testEquals() {
        assertEquals(new PartConnectionsInfo(NEEDS_CHASSIS_CONN, new HashSet<TerminalInfo>(Arrays.asList(TERMINAL_INFOS))), testPartConnInfo);
    }

    @Test
    public void testClone() {
        PartConnectionsInfo clone = testPartConnInfo.clone();
        assertEquals(testPartConnInfo, clone);
        assertNotSame(testPartConnInfo, clone);
    }
}