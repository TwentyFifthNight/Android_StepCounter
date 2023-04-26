package com.example.stepmeter.dbclasses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoElement {

    @Insert
    void insert(StepsCount element);

    @Query("DELETE FROM StepsList")
    void deleteAll();

    @Delete
    void delete(StepsCount element);

    @Query("SELECT * FROM StepsList ORDER BY date ASC")
    LiveData<List<StepsCount>> getAlphabetizedElements();
}
