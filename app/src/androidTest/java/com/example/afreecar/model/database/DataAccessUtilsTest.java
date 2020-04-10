package com.example.afreecar.model.database;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.Kit;
import com.example.afreecar.model.PartType;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.afreecar.model.database.DataAccessUtils.*;
import static org.junit.Assert.*;

public class DataAccessUtilsTest {

    private FirebaseFirestore db;

    private static final PartTag EXPECTED_PART_TAG = new PartTag(PartType.Controller, 1);
    private static final String TARGET_PART_TAG_DOC_ID = "controller 1";

    private static final PartRequirement EXPECTED_PART_REQ = new PartRequirement(
            EXPECTED_PART_TAG,
            new HashSet<ID>(Arrays.asList(new ID("1")))
    );
    private static final String TARGET_PART_REQ_DOC_ID = "0cM3O7wweJyhru2OOi7p";

    private static final String TARGET_KIT_DOC_ID = "0";

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void testBuildPartTagFromDoc() throws Exception {
        Task<DocumentSnapshot> task = db.collection(PartTag.COLLECTION_NAME)
                .document(TARGET_PART_TAG_DOC_ID)
                .get();

        DocumentSnapshot doc = tryGetDocResult(task);

        PartTag tag = buildPartTagFromDoc(doc);

        assertEquals(new PartTag(PartType.Controller, 1), tag);
    }

    @Test
    public void testBuildPartReqFromDoc() throws Exception {
        Task<DocumentSnapshot> task = db.collection(PartRequirement.COLLECTION_NAME)
                .document(TARGET_PART_REQ_DOC_ID)
                .get();

        DocumentSnapshot doc = tryGetDocResult(task);

        PartRequirement partReq = buildPartReqFromDoc(doc);

        assertEquals(EXPECTED_PART_REQ, partReq);
    }

    @Test
    public void testBuildKitFromDoc() throws Exception {
        Task<DocumentSnapshot> task = db.collection(Kit.COLLECTION_NAME)
                .document(TARGET_KIT_DOC_ID)
                .get();

        DocumentSnapshot doc = tryGetDocResult(task);

        Kit kit = buildKitFromDoc(doc);

        HashSet<PartRequirement> partReqs = (HashSet<PartRequirement>) kit.getPartRequirements();
        boolean result = partReqs.contains(EXPECTED_PART_REQ);

        assertTrue(kit.getPartRequirements().contains(EXPECTED_PART_REQ));
    }

    @Test
    public void testGetKitRequirements() throws Exception {
        Kit kit;

        kit = getKitRequirements(new ID(TARGET_KIT_DOC_ID));

        assertNotNull(kit);
    }
}
