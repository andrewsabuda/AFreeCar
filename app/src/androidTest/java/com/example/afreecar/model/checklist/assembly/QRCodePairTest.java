package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.ID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static com.example.afreecar.model.TestConstants.*;

public class QRCodePairTest {

    private QRCodePair testTermIDPair;

    @Before
    public void setUp() {
        testTermIDPair = new QRCodePair(CONTROLLER_TO_BATTERY_TERMINAL, BATTERY_TO_CONTROLLER_TERMINAL);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTermIDPair.writeToParcel(parcel, testTermIDPair.describeContents());
        parcel.setDataPosition(0);

        QRCodePair createdFromParcel = QRCodePair.CREATOR.createFromParcel(parcel);

        assertEquals(testTermIDPair, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testTermIDPair.describeContents());
    }

    @Test
    public void testEquals() {
        assertEquals(new QRCodePair(BATTERY_TO_CONTROLLER_TERMINAL, CONTROLLER_TO_BATTERY_TERMINAL), testTermIDPair);
    }

    @Test
    public void testClone() {
        QRCodePair clone = testTermIDPair.clone();
        assertEquals(testTermIDPair, clone);
        assertNotSame(testTermIDPair, clone);
    }
}
