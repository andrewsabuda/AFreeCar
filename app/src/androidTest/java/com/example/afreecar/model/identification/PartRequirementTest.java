package com.example.afreecar.model.identification;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.PartTag;
import com.example.afreecar.model.PartType;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class PartRequirementTest {

    private PartRequirement testReq;

    private static final PartTag TAG = new PartTag(PartType.Controller, 1);
    private static final Set<ID> VALID_IDS = setupTestValidIDs();

    private static Set<ID> setupTestValidIDs() {
        Set<ID> temp = new HashSet<ID>();

        temp.add(new ID("1234"));
        temp.add(new ID("5678"));

        return temp;
    }

    @Before
    public void setUp() {
        testReq = new PartRequirement(TAG, VALID_IDS);
    }

    @Test
    public void writeToParcel() {
        Parcel parcel = Parcel.obtain();
        testReq.writeToParcel(parcel, testReq.describeContents());
        parcel.setDataPosition(0);

        PartRequirement createdFromParcel = PartRequirement.CREATOR.createFromParcel(parcel);

        assertEquals(testReq, createdFromParcel);
    }

    @Test
    public void describeContents() {
        assertEquals(testReq.describeContents(), 0);
    }

    @Test
    public void getPartTag() {
        assertEquals(testReq.getPartTag(), TAG);
    }

    @Test
    public void getValidPartIDs() {
        assertEquals(testReq.getValidPartIDs(), VALID_IDS);
    }

    @Test
    public void testEquals() {
        assertEquals(new PartRequirement(TAG, VALID_IDS), testReq);
    }

    public void testClone() {
        PartRequirement clone = testReq.clone();
        assertEquals(testReq, clone);
        assertNotSame(testReq, clone);
    }
}
