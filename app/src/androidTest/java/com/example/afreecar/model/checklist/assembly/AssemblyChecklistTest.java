package com.example.afreecar.model.checklist.assembly;

import android.os.Parcel;

import com.example.afreecar.model.TestConstants;
import com.example.afreecar.model.checklist.PartTagPair;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.example.afreecar.model.TestConstants.*;

import static org.junit.Assert.*;

public class AssemblyChecklistTest {

    private AssemblyChecklist testAssemblyChecklist;

    @Before
    public void setUp() throws Exception {
        testAssemblyChecklist = new AssemblyChecklist(TestConstants.E_KIT);
    }

    @Test
    public void writeToParcel() {
        Parcel parcel = Parcel.obtain();
        testAssemblyChecklist.writeToParcel(parcel, testAssemblyChecklist.describeContents());
        parcel.setDataPosition(0);

        AssemblyChecklist createdFromParcel = AssemblyChecklist.CREATOR.createFromParcel(parcel);

        assertEquals(testAssemblyChecklist, createdFromParcel);
    }

    @Test
    public void testDescribeContents() {
        assertEquals(0, testAssemblyChecklist.describeContents());
    }

    @Test
    public void testGetValidDistancesMap() {
    }

    private static Map<PartTagPair, QRCodePair> constructExpectedValidDistancesMap() {
        Map<PartTagPair, QRCodePair> tempMap = new HashMap<PartTagPair, QRCodePair>(10);
        tempMap.put(new PartTagPair(CONTROLLER_TAG), new QRCodePair(CONTROLLER));

        return tempMap;
    }

    @Test
    public void testGetFulfilledElementsMap() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testClone() {
    }

    @Test
    public void testGetElements() {
    }

    @Test
    public void testDoesFulfill() {
    }

    @Test
    public void testTryFulfill() {
    }

    @Test
    public void testIsFulfilled() {
    }
}