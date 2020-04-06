package com.example.afreecar.model.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PartsAssemblyTest {

    private PartsAssembly testAssembly;

    private static final Map<PartTag, ID> PICKED_PARTS_MAP = initializePickedPartsMap();

    public static Map<PartTag, ID> initializePickedPartsMap() {
        Map<PartTag, ID> output = new HashMap<PartTag, ID>();

        //Todo

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