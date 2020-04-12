package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.example.afreecar.model.checklist.PartTag;
import com.google.firebase.firestore.FirebaseFirestore;

public class FRODAOFactory implements ReadOnlyDAOFactory<DAOs> {

    public static final FRODAOFactory READ_ONLY_FIRESTORE_DAO_FACTORY = new FRODAOFactory(FirebaseFirestore.getInstance());

    FirebaseFirestore firestore;

    public FRODAOFactory(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public AbstractFirestoreReadOnlyDAO<?> createDAO(@NonNull DAOs type) {
        switch (type) {
            case PartTag:
                return new PartTagFRODAO(firestore);

            case PartRequirement:
                return new PartRequirementFRODAO(firestore);

            case KitRequirements:
                return new KitRequirementsFRODAO(firestore);

            case Terminal:
                return new TerminalFRODAO(firestore);

            case Part:
                return new PartFRODAO(firestore);
        }

        return null;
    }
}
