package com.example.afreecar.model.database;

public interface ReadOnlyDAOFactory<E extends Enum<E>> {

    ReadOnlyDAO createDAO(E type);
}
