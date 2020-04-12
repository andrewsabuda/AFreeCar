package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
//import com.example.afreecar.model.Kit;
import com.example.afreecar.model.KitRequirements;
import com.example.afreecar.model.PartType;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.assembly.Part;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.DocumentKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Class containing static methods used to query the database / get demo data.
 */
public class DataAccessUtils {

    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();

//    public static void waitForTaskSuccess(Task task) throws Exception {
//        while(!task.isComplete()) {
//            if(task.isCanceled()) {
//                throw new Exception("Task was cancelled.");
//            }
//        }
//        if (!task.isSuccessful()) {
//            throw new Exception("Task was unsuccessful.");
//        }
//        task.continueWithTask()
//    }
//
//    public static DocumentSnapshot tryGetDocResult(Task<DocumentSnapshot> task) throws Exception {
//        try {
//            Tasks.await(task)
//            waitForTaskSuccess(task);
//        } catch (Exception e) {
//            throw new Exception("Failed to get document.", e);
//        }
//
//        return task.getResult();
//    }

//    public static DocumentSnapshot tryGetReferencedDoc(DocumentReference docRef) throws Exception {
//        Task<DocumentSnapshot> readTask = docRef.get();
//        DocumentSnapshot sourceDoc;
//
//        try {
//            sourceDoc = tryGetDocResult(readTask);
//        } catch (Exception e) {
//            throw new Exception("Failed to get referenced document.", e);
//        }
//
//        return sourceDoc;
//    }

    private static String toPascalCase(String string) {
        String output = string.toLowerCase().trim();
        output = output.substring(0, 1).toUpperCase() + output.substring(1);

        return output;
    }

//    public boolean isValidKitID(@NonNull ID kitID) {
//        // Todo actual database query implementation
//        return kitID.equals(DemoStuff.KIT_ID);
//    }

    public static ID buildIDFromDoc(@NonNull DocumentSnapshot doc) {
        return new ID(doc.getId());
    }

    public static PartTag buildPartTagFromDoc(@NonNull DocumentSnapshot doc) {
        PartTag tag;
        PartType type;
        Byte ordinal;

        DocumentReference typeRef = doc.getDocumentReference(PartTag.TYPE_FIELD_NAME);
        String partTypeID = toPascalCase(typeRef.getId());
        type = PartType.valueOf(partTypeID);

        DocumentReference ordinalRef = doc.getDocumentReference(PartTag.ORDINAL_FIELD_NAME);
        ordinal = Byte.valueOf(ordinalRef.getId());

        tag = new PartTag(type, ordinal);

        return tag;
    }

    public static Terminal buildTerminalFromDoc(@NonNull DocumentSnapshot doc) {
        Terminal terminal;
        ID terminalID;
        Double qrDistance;
        PartTag target = null;

        // Start  task
        Task<DocumentSnapshot> readTagTask = doc.getDocumentReference(Terminal.TARGET_TAG_FIELD_NAME).get();

        // Get available fields
        terminalID = new ID(doc.getDocumentReference(Terminal.ID_FIELD_NAME).getId());
        qrDistance = doc.getDouble(Terminal.QR_DISTANCE_FIELD_NAME);

        // Get task result
        try {
            DocumentSnapshot tagDoc = Tasks.await(readTagTask);
            target = buildPartTagFromDoc(tagDoc);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        terminal = new Terminal(terminalID, qrDistance, target);

        return terminal;
    }

    public static PartRequirement buildPartReqFromDoc(@NonNull DocumentSnapshot doc) {
        PartRequirement partReq;
        PartTag tag;
        Set<ID> validIDs;

        // Start task
        DocumentReference tagRef = doc.getDocumentReference(PartRequirement.PART_TAG_FIELD_NAME);
        DocumentSnapshot tagDoc = null;

        // Get available fields
        ArrayList<DocumentReference> validIDRefs = (ArrayList<DocumentReference>) doc.get(PartRequirement.VALID_IDS_FIELD_NAME);
        validIDs = new HashSet<ID>(validIDRefs.size());
        for(DocumentReference idRef: validIDRefs) {
            validIDs.add(new ID(idRef.getId()));
        }

        try {
            tagDoc = Tasks.await(tagRef.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tag = buildPartTagFromDoc(tagDoc);



        partReq = new PartRequirement(tag, validIDs); //new PartRequirement();

        return partReq;
    }

    public static KitRequirements buildKitFromDoc(@NonNull DocumentSnapshot doc) throws Exception {
        KitRequirements kit = null;
        ID kitID;
        Set<PartRequirement> partReqs;

        kitID = new ID(doc.getDocumentReference(KitRequirements.ID_FIELD_NAME).getId());

        ArrayList<DocumentReference> partReqRefs = (ArrayList<DocumentReference>) doc.get(KitRequirements.PART_REQS_FIELD_NAME);
        partReqs = new HashSet<PartRequirement>(partReqRefs.size());
        for(DocumentReference partReqRef: partReqRefs) {
            PartRequirement current;
            Task<DocumentSnapshot> readPartReqTask = partReqRef.get();
            try {
                DocumentSnapshot partReqDoc = Tasks.await(readPartReqTask);
                current = buildPartReqFromDoc(partReqDoc);
            } catch (Exception e) {
                throw new Exception("Failed to get referenced PartRequirement", e);
            }
            partReqs.add(current);
        }

        kit = new KitRequirements(kitID, partReqs);
        return kit;
    }

    /**
     * @param kitID - the ID of the kit whose requirements should be retreived.
     * @return an array of {@code PartRequirements} for the input KitID.
     */
    public static KitRequirements getKitRequirements(@NonNull ID kitID) throws Exception {
        KitRequirements kit = null;

        Task<DocumentSnapshot> readKitReqsTask = DB.collection(KitRequirements.COLLECTION_NAME)
                .document(kitID.toString())
                .get();

        try {
            DocumentSnapshot kitDoc = Tasks.await(readKitReqsTask);
            kit = buildKitFromDoc(kitDoc);
        } catch (Exception e) {
            throw new Exception("Failed to retrieve part requirements for input kit ID", e);
        }

        return kit;
    }

    /**
     * @param id - The {@Code ID} of the target part
     * @return The {@Code PartConnectionsInfo} containing the set of terminals associated with this part
     */
    public static Part getPartConnectionInfoForID(@NonNull ID id) {
        // Todo actual database query implementation
//        boolean needsChassisConnection = true;
//        Set<Terminal> terminals = new HashSet<Terminal>();
//
//        if (id.equals(DemoStuff.controllerID)) {
//            terminals = DemoStuff.controllerTerminals;
//        }
//        else if (id.equals(DemoStuff.batteryID)) {
//            terminals = DemoStuff.batteryTerminals;
//        }
//        else if (id.equals(DemoStuff.motorID)) {
//            terminals = DemoStuff.motorTerminals;
//        }
//        else {
//            needsChassisConnection = false;
//        }
//
        return null; //new Part(needsChassisConnection, terminals);
    }
}
