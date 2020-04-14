package com.example.afreecar.model;

import android.os.Parcel;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class EKitTest {

    private EKit testKit;

    @Before
    public void setUp() {
        testKit = TestConstants.E_KIT.clone();
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testKit.describeContents());
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testKit.writeToParcel(parcel, testKit.describeContents());
        parcel.setDataPosition(0);

        EKit createdFromParcel = EKit.CREATOR.createFromParcel(parcel);

        assertEquals(testKit, createdFromParcel);
    }

    @Test
    public void testGetID() {
        assertEquals(TestConstants.KIT_ID, testKit.getID());
    }

    @Test
    public void testGetPartsMap() {
        assertEquals(TestConstants.STANDARD_PICKED_PARTS_MAP, testKit.getPartsMap());
    }

    @Test
    public void testClone() {
        assertEquals(TestConstants.E_KIT, testKit);
        assertNotSame(TestConstants.E_KIT, testKit);
    }
}

