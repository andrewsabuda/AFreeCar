package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.PartTag;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class TerminalFRODAO extends AbstractFirestoreReadOnlyDAO<Terminal> {

    private static final String COLLECTION_NAME = "Terminals";
    private static final String ID_FIELD_NAME = "id";
    private static final String QR_DISTANCE_FIELD_NAME = "qr distance";
    private static final String TARGET_TAG_FIELD_NAME = "target tag";

    public TerminalFRODAO(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public Terminal createInstance(@NonNull DocumentSnapshot doc) {
        Terminal terminal;

        ID terminalID;
        Double qrDistance;
        PartTag target = null;

        // Start task
        AbstractFirestoreReadOnlyDAO<PartTag>.DAOReadTaskFirestoreSingle readTagsTask;
        PartTagFRODAO partTagFactory = new PartTagFRODAO(this.getFirestore());
        readTagsTask = partTagFactory.fetch(doc.getDocumentReference(TARGET_TAG_FIELD_NAME));
        Task<DocumentSnapshot> readTagTask = doc.getDocumentReference(TARGET_TAG_FIELD_NAME).get();

        // Get available fields
        terminalID = new ID(doc.getDocumentReference(ID_FIELD_NAME).getId());
        qrDistance = doc.getDouble(QR_DISTANCE_FIELD_NAME);

        // Get task result
        try {
            target = readTagsTask.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        terminal = new Terminal(terminalID, qrDistance, target);

        return terminal;
    }
}
