package com.revature.auldlangsyne.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GrocerDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Grocer grocer);

    @Query("SELECT * FROM grocers")
    LiveData<List<Grocer>> selectAll();

    @Query("SELECT * FROM grocers")
    Grocer selectByName();

    @Query("DELETE FROM grocers")
    void deleteAll();

    @Query("SELECT COUNT(id) FROM grocers")
    int getCount();

}
