package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.ID;
import com.example.afreecar.model.checklist.assembly.Part;
import com.example.afreecar.model.checklist.assembly.Terminal;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class PartFRODAO extends AbstractFirestoreReadOnlyDAO<Part> {

    private static final String COLLECTION_NAME = "Parts";

    private static final String ID_FIELD_NAME = "id";
    private static final String QR_DISTANCE_FIELD_NAME = "qr distance";
    private static final String TERMINALS_FIELD_NAME = "terminals";

    public PartFRODAO(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public Part createInstance(@NonNull DocumentSnapshot doc) {
        Part part;

        ID partID;
        Double qrDistance;
        List<Terminal> terminals = null;

        // Start async task
        AbstractFirestoreReadOnlyDAO<Terminal>.DAOReadTaskFirestoreList readTerminalsTask;
        TerminalFRODAO terminalRODAOFirestore = new TerminalFRODAO(getFirestore());
        List<DocumentReference> terminalRefs = (List<DocumentReference>) doc.get(TERMINALS_FIELD_NAME);
        readTerminalsTask = terminalRODAOFirestore.fetch(terminalRefs);

        // Get available fields
        partID = new ID(doc.getString(ID_FIELD_NAME));
        qrDistance = doc.getDouble(QR_DISTANCE_FIELD_NAME);

        // Get task results
        try {
            terminals = readTerminalsTask.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        part = new Part(partID, qrDistance, terminals);

        return part;
    }
}
