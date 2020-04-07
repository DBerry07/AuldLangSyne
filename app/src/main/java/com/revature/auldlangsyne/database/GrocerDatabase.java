package com.revature.auldlangsyne.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Grocer.class}, version = 1, exportSchema = false)
public abstract class GrocerDatabase extends RoomDatabase {

    public abstract GrocerDAO grocerDAO();

    private static volatile GrocerDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GrocerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GrocerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GrocerDatabase.class, "grocer_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
