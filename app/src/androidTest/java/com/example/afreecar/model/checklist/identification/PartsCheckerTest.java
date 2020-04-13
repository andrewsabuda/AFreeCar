package com.example.afreecar.model.checklist.identification;

import android.os.Parcel;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.KitRequirements;
import com.example.afreecar.model.checklist.PartTag;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.example.afreecar.model.TestConstants.*;

import static org.junit.Assert.*;

public class PartsCheckerTest {

    private PartsChecker testChecker;

    private static final PartTag[] PART_TAGS;
    static {
        PART_TAGS = new PartTag[] {
                CONTROLLER_TAG,
                BATTERY_TAG,
                MOTOR_1_TAG,
                MOTOR_2_TAG
        };
    }

    private static final ID[][] VALID_ID_SETS;
    static {
        VALID_ID_SETS = new ID[][] {
                new ID[]{
                        CONTROLLER_ID
                },
                new ID[] {
                        BATTERY_ID
                },
                new ID[]{
                        MOTOR_1_ID,
                        MOTOR_2_ID
                },
                new ID[]{
                        MOTOR_1_ID,
                        MOTOR_2_ID
                }
        };
    }

    private static PartRequirement[] getPartRequirements() {
        int length  = PART_TAGS.length;

        PartRequirement[] output = new PartRequirement[length];

        for (int i = 0; i < length; i++) {
            output[i] = new PartRequirement(PART_TAGS[i], new HashSet<ID>(Arrays.asList(VALID_ID_SETS[i])));
        }

        return output;
    }

    private static ID getLastValidPart(int i) {
        ID[] validIDs = VALID_ID_SETS[i];
        return validIDs[validIDs.length - 1];
    }

    private boolean pickPart(int i) {
        return testChecker.tryFulfill(PART_TAGS[i], getLastValidPart(i));
    }

    private void pickParts() {
        for (int i = 0; i < PART_TAGS.length; i++) {
            pickPart(i);
        }
    }

    @Before
    public void setUp() {
        testChecker = new PartsChecker(new KitRequirements(KIT_ID, new HashSet<PartRequirement>(Arrays.asList(getPartRequirements()))));
    }

    @Test
    public void testDescribeContents() {
        assertEquals(testChecker.describeContents(), 0);
    }

    @Test
    public void testWriteToParcel() {
        pickParts();

        Parcel parcel = Parcel.obtain();
        testChecker.writeToParcel(parcel, testChecker.describeContents());
        parcel.setDataPosition(0);

        PartsChecker createdFromParcel = PartsChecker.CREATOR.createFromParcel(parcel);

        assertEquals(testChecker, createdFromParcel);
    }

    @Test
    public void testGetPartTags() {
        assertEquals(new ArrayList<PartTag>(Arrays.asList(PART_TAGS)), testChecker.getElements());
    }

    @Test
    public void testGetPickedPart() {
        for (PartTag tag: PART_TAGS) {
            assertEquals(testChecker.getPickedPartID(tag), null);
        }

        pickParts();

        for (int i = 0; i < PART_TAGS.length; i++) {
            assertEquals(testChecker.getPickedPartID(PART_TAGS[i]), getLastValidPart(i));
        }
    }

    @Test
    public void testGetValidParts() {
        for (int i = 0; i < PART_TAGS.length; i++) {
            assertEquals(testChecker.getValidParts(PART_TAGS[i]), new HashSet<ID>(Arrays.asList(VALID_ID_SETS[i])));
        }
    }

    @Test
    public void testIsPartPicked() {
        for (PartTag tag: PART_TAGS) {
            assertFalse(testChecker.isFulfilled(tag));
        }

        pickParts();

        for (PartTag tag: PART_TAGS) {
            assertTrue(testChecker.isFulfilled(tag));
        }
    }

    @Test
    public void testTryFulfill() {
        for (int i = 0; i < PART_TAGS.length; i++) {
            PartTag currentTag = PART_TAGS[i];
            assertFalse(testChecker.isFulfilled(currentTag));
            assertTrue(pickPart(i));
            assertEquals(testChecker.getPickedPartID(currentTag), getLastValidPart(i));
        }
    }

    @Test
    public void testTryRemovePart() {
        pickParts();

        for (int i = 0; i < PART_TAGS.length; i++) {
            PartTag currentTag = PART_TAGS[i];
            assertTrue(testChecker.isFulfilled(currentTag));
            assertTrue(testChecker.tryRemovePart(currentTag));
            assertFalse(testChecker.isFulfilled(currentTag));
        }
    }

    @Test
    public void testIsChecklistFulfilled() {
        for (int i = 0; i < PART_TAGS.length; i++) {
            assertFalse(testChecker.isChecklistFulfilled());
            pickPart(i);
        }

        assertTrue(testChecker.isChecklistFulfilled());
    }



    @Test
    public void testClonePickedParts() {
        pickParts();

        Map<PartTag, ID> clonedIDs = testChecker.clonePickedParts();

        Map<PartTag, ID> pickedPartsMap = new HashMap<>(clonedIDs.size());

        for (PartTag tag: testChecker.getElements()) {
            pickedPartsMap.put(tag, testChecker.getPickedPartID(tag));
        }

        assertEquals(pickedPartsMap, clonedIDs);
    }

    @Test
    public void testEquals() {
        assertEquals(new PartsChecker(KIT_REQUIREMENTS), testChecker);
    }

    @Test
    public void testClone() {
        PartsChecker clone = testChecker.clone();
        assertEquals(testChecker, clone);
        assertNotSame(testChecker, clone);

        pickParts();

        assertNotEquals(testChecker, clone);
    }
}
