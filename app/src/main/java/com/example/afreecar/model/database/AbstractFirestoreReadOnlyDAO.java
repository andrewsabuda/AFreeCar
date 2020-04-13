package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.firebase.firestore.FieldPath.documentId;

/**
 * Abstract class defining methods for retrieving a specific type of object from a Firestore database.
 * @param <TObject> The type of object that this DAO will retrieve.
 */
public abstract class AbstractFirestoreReadOnlyDAO<TObject> implements ReadOnlyDAO<TObject, String> {

    @NonNull private final FirebaseFirestore firestore;

    public AbstractFirestoreReadOnlyDAO(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    private CollectionReference getCollection() {
        CollectionReference collectionReference;

        collectionReference = this.firestore.collection(this.getCollectionName());

        return collectionReference;
    }

    public DAOReadTaskFirestoreSingle fetch(@NonNull DocumentReference documentReference) {
        DAOReadTaskFirestoreSingle daoReadTaskFirestoreSingle;

        Task<DocumentSnapshot> docReadTask = documentReference.get();

        daoReadTaskFirestoreSingle = new DAOReadTaskFirestoreSingle(docReadTask);

        return daoReadTaskFirestoreSingle;
    }

    @Override
    public DAOReadTaskFirestoreSingle fetch(@NonNull String id) {
        DAOReadTaskFirestoreSingle daoReadTaskFirestoreSingle;

        CollectionReference collectionReference = this.getCollection();

        DocumentReference docRef = collectionReference.document(id);

        daoReadTaskFirestoreSingle = fetch(docRef);

        return daoReadTaskFirestoreSingle;
    }

    public DAOReadTaskFirestoreList fetch(@NonNull Query query) {
        DAOReadTaskFirestoreList daoReadTaskFirestoreList;

        Task<QuerySnapshot> docsReadTask = query.get();

        daoReadTaskFirestoreList = new DAOReadTaskFirestoreList(docsReadTask);

        return daoReadTaskFirestoreList;
    }

    public DAOReadTaskFirestoreList fetch(@NonNull Collection<DocumentReference> documentReferences) {
        DAOReadTaskFirestoreList daoReadTaskFirestoreList;

        List<String> docIds = new ArrayList<String>(documentReferences.size());

        for (DocumentReference docRef: documentReferences) {
            docIds.add(docRef.getId());
        }

        daoReadTaskFirestoreList = this.fetch(docIds);

        return daoReadTaskFirestoreList;
    }

    public DAOReadTaskFirestoreList fetch(@NonNull List<String> ids) {
        DAOReadTaskFirestoreList daoReadTaskFirestoreList;

        CollectionReference collectionReference = this.getCollection();

        Query query = collectionReference.whereIn(documentId(), ids);

        daoReadTaskFirestoreList = fetch(query);

        return daoReadTaskFirestoreList;
    }

    @Override
    public DAOReadTaskFirestoreList fetch(@NonNull String... ids) {
        DAOReadTaskFirestoreList daoReadTaskFirestoreList;

        List<String> idsList = Arrays.asList(ids);

        daoReadTaskFirestoreList = this.fetch(ids);

        return daoReadTaskFirestoreList;
    }

    public abstract String getCollectionName();

    public abstract TObject createInstance(@NonNull DocumentSnapshot doc);

    class DAOReadTaskFirestoreSingle implements DAOReadTask<TObject> {

        private Task<DocumentSnapshot> readTask;

        public DAOReadTaskFirestoreSingle(Task<DocumentSnapshot> readTask) {
            this.readTask = readTask;
        }

        @Override
        public TObject getResult() throws IOException {
            TObject result;

            DocumentSnapshot doc;
            try {
                doc = Tasks.await(readTask);
            } catch (ExecutionException e) {
                throw new IOException(e);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }

            result = createInstance(doc);

            return result;
        }
    }

    class DAOReadTaskFirestoreList implements DAOReadTask<List<TObject>> {

        private Task<QuerySnapshot> readsTask;

        public DAOReadTaskFirestoreList(Task<QuerySnapshot> readsTask) {
            this.readsTask = readsTask;
        }

        @Override
        public List<TObject> getResult() throws IOException {
            List<TObject> result;

            QuerySnapshot docs;
            try {
                docs = Tasks.await(readsTask);
            } catch (ExecutionException e) {
                throw new IOException(e);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }

            result = new ArrayList<TObject>(docs.size());

            for (DocumentSnapshot doc: docs.getDocuments()) {
                TObject createdInstance = createInstance(doc);
                result.add(createdInstance);
            }

            return result;
        }
    }
}
