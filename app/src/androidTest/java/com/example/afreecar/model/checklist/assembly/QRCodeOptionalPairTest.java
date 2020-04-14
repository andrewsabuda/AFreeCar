package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static com.example.afreecar.model.TestConstants.*;

public class QRCodeOptionalPairTest {

    private QRCodePair testTermPair;
    private QRCodePair testPartChassisPair;

    @Before
    public void setUp() {
        testTermPair = new QRCodePair(CONTROLLER_TO_BATTERY_TERMINAL, BATTERY_TO_CONTROLLER_TERMINAL);
        testPartChassisPair = new QRCodePair(CONTROLLER);
    }

    @Test
    public void testWriteToParcel() {
        Parcel parcel = Parcel.obtain();
        testTermPair.writeToParcel(parcel, testTermPair.describeContents());
        testPartChassisPair.writeToParcel(parcel, testPartChassisPair.describeContents());
        parcel.setDataPosition(0);

        QRCodePair termPairFromParcel = QRCodePair.CREATOR.createFromParcel(parcel);
        assertEquals(testTermPair, termPairFromParcel);

        QRCodePair partChassisPairFromParcel = QRCodePair.CREATOR.createFromParcel(parcel);
        assertEquals(testPartChassisPair, partChassisPairFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testTermPair.describeContents());
    }

    @Test
    public void testEquals() {
        assertEquals(new QRCodePair(BATTERY_TO_CONTROLLER_TERMINAL, CONTROLLER_TO_BATTERY_TERMINAL), testTermPair);
        assertEquals(new QRCodePair(CONTROLLER), testPartChassisPair);
    }

    @Test
    public void testGetQRDistance() {
        assertEquals(CONTROLLER_TO_BATTERY_TERMINAL.getQRDistance() + BATTERY_TO_CONTROLLER_TERMINAL.getQRDistance(), testTermPair.getQRDistance(), 0);
    }

    @Test
    public void testClone() {
        QRCodePair clone = testTermPair.clone();
        assertEquals(testTermPair, clone);
        assertNotSame(testTermPair, clone);
    }
}
