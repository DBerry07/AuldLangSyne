package com.revature.auldlangsyne.database;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GrocerRepository {

    private GrocerDatabase db;
    private GrocerDAO grocerDAO;
    private LiveData<List<Grocer>> grocerList;

    GrocerRepository(Application application) {
        db = GrocerDatabase.getDatabase(application);
        grocerDAO = db.grocerDAO();
        grocerList = grocerDAO.selectAll();
    }

    void insert(final Grocer grocer) {
        GrocerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                grocerDAO.insert(grocer);
            }
        });
    }

    int getCount() {
        return grocerList.getValue().size();
    }

    LiveData<List<Grocer>> getAll() {
        return grocerList;
    }

    Grocer getGrocer(String grocerName) {
        for (Grocer each : grocerList.getValue()){
            if (grocerName.equals(each.getName())){
                return each;
            }
        }
        return null;
    }

}
