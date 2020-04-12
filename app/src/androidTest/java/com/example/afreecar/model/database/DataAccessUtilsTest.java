package com.example.afreecar.model.database;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.KitRequirements;
import com.example.afreecar.model.PartType;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.afreecar.model.TestConstants.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.example.afreecar.model.database.DataAccessUtils.*;
import static org.junit.Assert.*;

public class DataAccessUtilsTest {

    private FirebaseFirestore db;

    private static final String TARGET_PART_TAG_DOC_ID = "controller 1";

    private static final String TARGET_PART_REQ_DOC_ID = "0cM3O7wweJyhru2OOi7p";

    private static final String TARGET_KIT_DOC_ID = "0";

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void testBuildPartTagFromDoc() throws ExecutionException, InterruptedException {
        Task<DocumentSnapshot> readTagTask = db.collection(PartTag.COLLECTION_NAME)
                .document(TARGET_PART_TAG_DOC_ID)
                .get();

        DocumentSnapshot doc = Tasks.await(readTagTask);

        PartTag tag = buildPartTagFromDoc(doc);

        assertEquals(new PartTag(PartType.Controller, 1), tag);
    }

    @Test
    public void testBuildPartReqFromDoc() throws Exception {
        Task<DocumentSnapshot> task = db.collection(PartRequirement.COLLECTION_NAME)
                .document(TARGET_PART_REQ_DOC_ID)
                .get();

        DocumentSnapshot doc = Tasks.await(task);

        PartRequirement partReq = buildPartReqFromDoc(doc);

        assertEquals(CONTROLLER_REQ, partReq);
    }

    @Test
    public void testBuildKitRequirementsFromDoc() throws Exception {
        Task<DocumentSnapshot> task = db.collection(KitRequirements.COLLECTION_NAME)
                .document(TARGET_KIT_DOC_ID)
                .get();

        DocumentSnapshot doc = Tasks.await(task);

        KitRequirements actual = buildKitFromDoc(doc);

        KitRequirements expected = KIT_REQUIREMENTS;

        assertEquals(expected, actual);
    }

    @Test
    public void testGetKitRequirements() throws Exception {
        KitRequirements kit;

        kit = getKitRequirements(new ID(TARGET_KIT_DOC_ID));

        assertNotNull(kit);
    }
}
