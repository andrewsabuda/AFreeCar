package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.PartType;
import com.example.afreecar.model.checklist.PartTag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PartTagFRODAO extends AbstractFirestoreReadOnlyDAO<PartTag> {

    private static final String COLLECTION_NAME = "PartTags";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String ORDINAL_FIELD_NAME = "ordinal";

    public PartTagFRODAO(FirebaseFirestore firestore) {
        super(firestore);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public PartTag createInstance(@NonNull DocumentSnapshot doc) {
        PartTag tag;

        PartType type;
        Byte ordinal;

        DocumentReference typeRef = doc.getDocumentReference(TYPE_FIELD_NAME);
        String partTypeID = toPascalCase(typeRef.getId());
        type = PartType.valueOf(partTypeID);

        DocumentReference ordinalRef = doc.getDocumentReference(ORDINAL_FIELD_NAME);
        ordinal = Byte.valueOf(ordinalRef.getId());

        tag = new PartTag(type, ordinal);

        return tag;
    }

    private static String toPascalCase(String string) {
        String output = string.toLowerCase().trim();
        output = output.substring(0, 1).toUpperCase() + output.substring(1);

        return output;
    }
}
