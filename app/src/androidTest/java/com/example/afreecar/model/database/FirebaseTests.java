package com.example.afreecar.model.database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseTests {

    private FirebaseFirestore db;

    private Map<String, Object> docObject;

    private String TEST_COLLECTION = "_tests_";

//    private static OnCompleteListener<QuerySnapshot> listener = new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//            System.out.println("Now we're getting somewhere");
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    document.toObject(HashMap.class);
//                    Log.d(TAG, document.getId() + " => " + document.getData());
//                }
//            }
//            else {
//                Log.w(TAG, "Error getting documents.", task.getException());
//            }
//        }
//    };

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
