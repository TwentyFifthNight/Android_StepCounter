package com.example.stepmeter;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.stepmeter.dbclasses.DaoElement;
import com.example.stepmeter.dbclasses.StepsCount;
import com.example.stepmeter.dbclasses.StepsCountRoomDatabase;

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
        return mAllElements;
    }

    void deleteAll() {
        StepsCountRoomDatabase.databaseWriteExecutor.execute(mDaoElement::deleteAll);
    }

    void insert(StepsCount element){
        StepsCountRoomDatabase.databaseWriteExecutor.execute(() -> mDaoElement.insert(element));
    }

    void delete(StepsCount element){
        StepsCountRoomDatabase.databaseWriteExecutor.execute(() -> mDaoElement.delete(element));
    }
}
