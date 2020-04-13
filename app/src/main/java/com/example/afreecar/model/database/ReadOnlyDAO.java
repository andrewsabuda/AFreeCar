package com.example.afreecar.model.database;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining a class that handles reading data from a database to create a type of object.
 * @param <TObject> The type of object this ReadOnlyDAO will fetch.
 * @param <TIdentifier> The type used as a key for selecting the corresponding entities from the database.
 */
public interface ReadOnlyDAO<TObject, TIdentifier> {

    /**
     * Retrieves the TObject corresponding to the input TIdentifier.
     * @param id The TIdentifier of the TObject to be fetched.
     * @return The TObject assigned to the input TIdentifier.
     */
    DAOReadTask<TObject> fetch(@NonNull TIdentifier id);

    /**
     * Retrieves a list of TObjects corresponding to the input TIdentifiers
     * @param ids The TIdentifiers of the TObjects to be fetched.
     * @return The TObjects assigned to the input TIdentifiers.
     */
    DAOReadTask<List<TObject>> fetch(@NonNull TIdentifier... ids);

    /**
     * Retrieves a list of TObjects corresponding to the input TIdentifiers
     * @param ids The TIdentifiers of the TObjects to be fetched.
     * @return The TObjects assigned to the input TIdentifiers.
     */
    DAOReadTask<List<TObject>> fetch(@NonNull List<TIdentifier> ids);

    /**
     * Interface defining an in-progress read transaction from the database, which may be used to
     * asynchronously retrieve the result of that transaction once it is needed.
     * @param <T> The type of object that this task will retrieve.
     */
    interface DAOReadTask<T> {

        /**
         * Retrieves the result of a read transaction once it is completed.
         * @return the result of the read.
         * @throws IOException
         */
        T getResult() throws IOException;

    }
}
