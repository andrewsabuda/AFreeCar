package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.afreecar.model.TestConstants.*;

import static org.junit.Assert.*;

public class PartsAssemblyTest {

    private PartsAssembly testAssembly;

    private static final Map<PartTag, Part> PICKED_PARTS_MAP = initializePickedPartsMap();

    public static Map<PartTag, Part> initializePickedPartsMap() {
        Map<PartTag, Part> output = new HashMap<PartTag, Part>();

        output.put(CONTROLLER_TAG, CONTROLLER);
        output.put(BATTERY_TAG, BATTERY);
        output.put(MOTOR_1_TAG, MOTOR_2);
        output.put(MOTOR_2_TAG, MOTOR_1);

        return output;
    }

    @Before
    public void setUp() {
        testAssembly = new PartsAssembly(PICKED_PARTS_MAP);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testAssembly.writeToParcel(parcel, testAssembly.describeContents());
        parcel.setDataPosition(0);

        PartsAssembly createdFromParcel = PartsAssembly.CREATOR.createFromParcel(parcel);

        assertEquals(testAssembly, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testAssembly.describeContents());
    }

    @Test
    public void testGetTerminalConnectionStatus() {
    }

    @Test
    public void testTryUpdateTerminalConnectionStatus() {
    }

    @Test
    public void testNeedsChassisConnection() {
    }

    @Test
    public void testGetChassisConnectionStatus() {
    }

    @Test
    public void testTryUpdateChassisConnectionStatus() {
    }

    @Test
    public void testEquals() {
        assertEquals(new PartsAssembly(PICKED_PARTS_MAP), testAssembly);
    }

    @Test
    public void testClone() {
        PartsAssembly clone = testAssembly.clone();

        assertEquals(testAssembly, clone);
        assertNotSame(testAssembly, clone);
    }
}