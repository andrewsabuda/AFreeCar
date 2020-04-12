package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PartRequirementFRODAO extends AbstractFirestoreReadOnlyDAO<PartRequirement> {

    private static String COLLECTION_NAME = "PartRequirements";

    private static final String PART_TAG_FIELD_NAME = "part tag";
    private static final String VALID_IDS_FIELD_NAME = "valid ids";

    public PartRequirementFRODAO(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public PartRequirement createInstance(@NonNull DocumentSnapshot doc) {
        PartRequirement partReq;

        PartTag tag = null;
        Set<ID> validIDs;

        // Start task
        AbstractFirestoreReadOnlyDAO<PartTag>.DAOReadTaskFirestoreSingle readTagTask;
        PartTagFRODAO partTagRODAOFirestore = new PartTagFRODAO(getFirestore());
        readTagTask = partTagRODAOFirestore.fetch(doc.getDocumentReference(PartRequirement.PART_TAG_FIELD_NAME));

        // Get available fields
        ArrayList<DocumentReference> validIDRefs = (ArrayList<DocumentReference>) doc.get(PartRequirement.VALID_IDS_FIELD_NAME);
        validIDs = new HashSet<ID>(validIDRefs.size());
        for(DocumentReference idRef: validIDRefs) {
            validIDs.add(new ID(idRef.getId()));
        }

        try {
            tag = readTagTask.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        partReq = new PartRequirement(tag, validIDs);

        return partReq;
    }
}
