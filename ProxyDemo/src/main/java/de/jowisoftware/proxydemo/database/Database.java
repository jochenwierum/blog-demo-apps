package de.jowisoftware.proxydemo.database;

public interface Database {
    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();
}
