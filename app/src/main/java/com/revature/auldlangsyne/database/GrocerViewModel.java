package com.revature.auldlangsyne.database;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.List;

public class GrocerViewModel extends AndroidViewModel {

    private GrocerRepository grocerRepository;
    public LiveData<List<Grocer>> allGrocers;
    private static volatile GrocerViewModel INSTANCE;

    public GrocerViewModel(Application application){
        super(application);
        grocerRepository = new GrocerRepository(application);
        allGrocers = grocerRepository.getAll();
    }

    public LiveData<List<Grocer>> getGrocers() {
        if (allGrocers == null) {
            allGrocers = new MutableLiveData<List<Grocer>>();
        }
        return allGrocers;
    }

    public void insert(Grocer grocer){
        grocerRepository.insert(grocer);
    }

    public Grocer getGrocer(String name){
        return grocerRepository.getGrocer(name);
    }

    public LiveData<List<Grocer>> getAllGrocers() { return allGrocers; }

    public int getCount() { return grocerRepository.getCount(); }

    public static GrocerViewModel getInstance(final Activity fragment) {
        if (INSTANCE == null) {
            synchronized (GrocerViewModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GrocerViewModel(fragment.getApplication());
                    //INSTANCE = new ViewModelProvider(fragment).get(GrocerViewModel.class);
                }
            }
        }
        return INSTANCE;
    }
}
