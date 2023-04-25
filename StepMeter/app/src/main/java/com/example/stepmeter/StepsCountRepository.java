package com.example.stepmeter;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StepsCountRepository {
    private final DaoElement mDaoElement;
    private final LiveData<List<StepsCount>> mAllElements;

    public StepsCountRepository(Application application) {
        StepsCountRoomDatabase stepsCountRoomDatabase =
                StepsCountRoomDatabase.getDatabase(application);
        mDaoElement = stepsCountRoomDatabase.elementDao();
        mAllElements = mDaoElement.getAlphabetizedElements();
    }

    LiveData<List<StepsCount>> getAllElements() {
        //… metdoda zwraca wszystkie elementy
        return mAllElements;
    }

    void deleteAll() {
        //… skasowanie wszystkich elementów za pomocą
        //obiektuDAO
        StepsCountRoomDatabase.databaseWriteExecutor.execute(mDaoElement::deleteAll);
    }

    void insert(StepsCount element){
        StepsCountRoomDatabase.databaseWriteExecutor.execute(() -> mDaoElement.insert(element));
    }

    void delete(StepsCount element){
        StepsCountRoomDatabase.databaseWriteExecutor.execute(() -> mDaoElement.delete(element));
    }
}
