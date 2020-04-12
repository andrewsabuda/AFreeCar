package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.KitRequirements;
import com.example.afreecar.model.checklist.identification.PartRequirement;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class KitRequirementsFRODAO extends AbstractFirestoreReadOnlyDAO<KitRequirements> {

    private static final String COLLECTION_NAME = "Kits";

    private static final String ID_FIELD_NAME = "id";
    private static final String PART_REQS_FIELD_NAME = "part requirements";

    public KitRequirementsFRODAO(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public KitRequirements createInstance(@NonNull DocumentSnapshot doc) {
        KitRequirements kit = null;

        ID kitID;
        List<PartRequirement> partReqs = null;

        // Start task
        AbstractFirestoreReadOnlyDAO<PartRequirement>.DAOReadTaskFirestoreList readPartReqsTask;
        PartRequirementFRODAO partRequirementRODAOFirestore = new PartRequirementFRODAO(getFirestore());
        List<DocumentReference> partReqRefs = (List<DocumentReference>) doc.get(KitRequirements.PART_REQS_FIELD_NAME);
        readPartReqsTask = partRequirementRODAOFirestore.fetch(partReqRefs);

        // Get available fields
        kitID = new ID(doc.getDocumentReference(KitRequirements.ID_FIELD_NAME).getId());

        // Get task result
        try {
            partReqs = readPartReqsTask.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        kit = new KitRequirements(kitID, partReqs);
        return kit;
    }
}
