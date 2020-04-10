package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.DemoStuff;
import com.example.afreecar.model.ID;
import com.example.afreecar.model.Kit;
import com.example.afreecar.model.PartType;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.assembly.PartConnectionsInfo;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class containing static methods used to query the database / get demo data.
 */
public class DataAccessUtils {

    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public static void waitForTaskSuccess(Task task) throws Exception {
        // Run empty loop until task is complete
        while(!task.isComplete()) {}

        if (!task.isSuccessful()) {
            throw new Exception("Task was unsuccessful.");
        }
    }

    public static DocumentSnapshot tryGetDocResult(Task<DocumentSnapshot> task) throws Exception {
        try {
            waitForTaskSuccess(task);
        } catch (Exception e) {
            throw new Exception("Failed to get document.", e);
        }

        return task.getResult();
    }

    public static DocumentSnapshot tryGetReferencedDoc(DocumentReference docRef) throws Exception {
        Task<DocumentSnapshot> readTask = docRef.get();
        DocumentSnapshot sourceDoc;

        try {
            sourceDoc = tryGetDocResult(readTask);
        } catch (Exception e) {
            throw new Exception("Failed to get referenced document.", e);
        }

        return sourceDoc;
    }

    private static String toPascalCase(String string) {
        String output = string.toLowerCase().trim();
        output = output.substring(0, 1).toUpperCase() + output.substring(1);

        return output;
    }

    public boolean isValidKitID(@NonNull ID kitID) {
        // Todo actual database query implementation
        return kitID.equals(DemoStuff.kitID);
    }

    public static ID buildIDFromDoc(@NonNull DocumentSnapshot doc) {
        return new ID(doc.getId());
    }

    public static PartTag buildPartTagFromDoc(@NonNull DocumentSnapshot doc) {
        PartTag tag;

        DocumentReference typeRef = doc.getDocumentReference(PartTag.TYPE_FIELD_NAME);
        String partTypeID = toPascalCase(typeRef.getId());
        PartType type = PartType.valueOf(partTypeID);

        DocumentReference ordinalRef = doc.getDocumentReference(PartTag.ORDINAL_FIELD_NAME);
        byte ordinal = Byte.valueOf(ordinalRef.getId());

        tag = new PartTag(type, ordinal);

        return tag;
    }

    public static PartRequirement buildPartReqFromDoc(@NonNull DocumentSnapshot doc) throws Exception {
        PartRequirement partReq;
        PartTag tag;
        Set<ID> validIDs;

        DocumentReference tagRef = doc.getDocumentReference(PartRequirement.PART_TAG_FIELD_NAME);
        DocumentSnapshot tagDoc;
        try {
            tagDoc = tryGetReferencedDoc(tagRef);
        } catch (Exception e) {
            throw new Exception("Failed to get referenced PartTag", e);
        }
        tag = buildPartTagFromDoc(tagDoc);

        ArrayList<DocumentReference> validIDRefs = (ArrayList<DocumentReference>) doc.get(PartRequirement.VALID_IDS_FIELD_NAME);
        validIDs = new HashSet<ID>(validIDRefs.size());
        for(DocumentReference idRef: validIDRefs) {
            validIDs.add(new ID(idRef.getId()));
        }

        partReq = new PartRequirement(tag, validIDs); //new PartRequirement();

        return partReq;
    }

    public static Kit buildKitFromDoc(@NonNull DocumentSnapshot doc) throws Exception {
        Kit kit = null;
        ID kitID;
        Set<PartRequirement> partReqs;

        kitID = new ID(doc.getDocumentReference(Kit.ID_FIELD_NAME).getId());

        ArrayList<DocumentReference> partReqRefs = (ArrayList<DocumentReference>) doc.get(Kit.PART_REQS_FIELD_NAME);
        partReqs = new HashSet<PartRequirement>(partReqRefs.size());
        for(DocumentReference partReqRef: partReqRefs) {
            PartRequirement current;
            try {
                current = buildPartReqFromDoc(tryGetReferencedDoc(partReqRef));
            } catch (Exception e) {
                throw new Exception("Failed to get referenced PartRequirement", e);
            }
            partReqs.add(current);
        }

        kit = new Kit(kitID, partReqs);
        return kit;
    }

    /**
     * @param kitID - the ID of the kit whose requirements should be retreived.
     * @return an array of {@code PartRequirements} for the input KitID.
     */
    public static Kit getKitRequirements(@NonNull ID kitID) throws Exception {
        Kit kit = null;

        Task<DocumentSnapshot> readTask = DB.collection(Kit.COLLECTION_NAME)
                .document(kitID.toString())
                .get();

        try {
            kit = buildKitFromDoc(tryGetDocResult(readTask));
        } catch (Exception e) {
            throw new Exception("Failed to retrieve part requirements for input kit ID", e);
        }

        return kit;
    }

    /**
     * @param id - The {@Code ID} of the target part
     * @return The {@Code PartConnectionsInfo} containing the set of terminals associated with this part
     */
    public static PartConnectionsInfo getPartConnectionInfoForID(@NonNull ID id) {
        // Todo actual database query implementation
        boolean needsChassisConnection = true;
        Set<Terminal> terminals = new HashSet<Terminal>();

        if (id.equals(DemoStuff.controllerID)) {
            terminals = DemoStuff.controllerTerminals;
        }
        else if (id.equals(DemoStuff.batteryID)) {
            terminals = DemoStuff.batteryTerminals;
        }
        else if (id.equals(DemoStuff.motorID)) {
            terminals = DemoStuff.motorTerminals;
        }
        else {
            needsChassisConnection = false;
        }

        return new PartConnectionsInfo(needsChassisConnection, terminals);
    }
}
