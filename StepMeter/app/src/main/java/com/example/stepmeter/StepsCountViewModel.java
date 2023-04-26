package com.example.stepmeter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepmeter.dbclasses.StepsCount;

import java.util.List;

public class StepsCountViewModel extends AndroidViewModel {

    private final StepsCountRepository mRepository;
    private final LiveData<List<StepsCount>> mAllElements;

    public StepsCountViewModel(@NonNull Application application) {
        super(application);
        mRepository = new StepsCountRepository(application);
        mAllElements = mRepository.getAllElements();
    }

    public LiveData<List<StepsCount>> getAllElements() {
        return mAllElements;
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void insert(StepsCount element){
        mRepository.insert(element);
    }

    public void delete(StepsCount element){
        mRepository.delete(element);
    }
}
