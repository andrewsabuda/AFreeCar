package com.example.afreecar.model.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FirebaseTests {

    private FirebaseFirestore db;

    private Map<String, Object> docObject;

    private String TEST_COLLECTION = "_tests_";

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @Test
    public void testAnything() {

        String testName = db.getApp().getName();
        System.out.println(testName);
    }

    @Test
    @Ignore
    public void testSave() {
        CollectionReference testRef = db.collection(TEST_COLLECTION);
        Task<DocumentReference> task = testRef.add(getHelloWorldMap());

        waitForTaskComplete(task);
        assertTrue(task.isComplete());
        assertTrue(task.isSuccessful());
    }

    @Test
    public void testRead() {
        CollectionReference testRef = db.collection(TEST_COLLECTION);

        Task<QuerySnapshot> task = testRef.get();

        waitForTaskComplete(task);
        assertTrue(task.isSuccessful());

        List<DocumentSnapshot> retrievedDocs = task.getResult().getDocuments();
        List<String> docIds = new ArrayList<String>(retrievedDocs.size());
        for(DocumentSnapshot doc: retrievedDocs) {
            docIds.add((String) doc.get("hello"));
        }
        assertTrue(docIds.size() > 0);
    }

    public static Map<String, Object> getHelloWorldMap() {
        Map<String, Object> docFieldsMap = new HashMap<String, Object>();
        docFieldsMap.put("hello", "world");
        return docFieldsMap;
    }

    private static void waitForTaskComplete(Task task) {
        int i = 0;
        while(!task.isComplete()) {
            System.out.println("Waited " + String.valueOf(++i) + " cycles for task completion");
        }
    }
}
