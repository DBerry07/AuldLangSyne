package com.revature.auldlangsyne;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.revature.auldlangsyne.database.GrocerDAO;
import com.revature.auldlangsyne.database.GrocerDatabase;

import java.lang.ref.WeakReference;

class DatabaseAsyncTask extends AsyncTask<Void, Void, Integer> {

    //Prevent leak
    private WeakReference<Activity> weakActivity;

    public DatabaseAsyncTask(Activity activity) {
        weakActivity = new WeakReference<>(activity);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        GrocerDAO grocerDAO = GrocerDatabase.getDatabase(weakActivity.get().getApplicationContext()).grocerDAO();
        return grocerDAO.getCount();
    }

    @Override
    protected void onPostExecute(Integer grocerCount) {
        Activity activity = weakActivity.get();
        if(activity == null) {
            return;
        }

        if (grocerCount > 0) {
            //2: If it already exists then prompt user
            Toast.makeText(activity, "Database contains Grocers", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Database is empty", Toast.LENGTH_LONG).show();
            activity.onBackPressed();
        }
    }
}
