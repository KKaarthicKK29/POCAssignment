package com.example.karthickmadasamy.myapplication.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.karthickmadasamy.myapplication.db.FeederEntity;
import com.example.karthickmadasamy.myapplication.repo.FeederRepository;

import java.util.List;

/**
 * Created by Karthick.Madasamy on 12/27/2019.
 */

public class FeederViewModel extends AndroidViewModel{

    private FeederRepository repository;
    public FeederViewModel(@NonNull Application application) {
        super(application);
        repository = new FeederRepository(application.getApplicationContext());

    }

    public void insert(FeederEntity entity) {
        repository.insert(entity);
    }

    public void insertAll(List<FeederEntity> entities) {
        repository.insertAll(entities);
    }

    public LiveData<List<FeederEntity>> getAllFeeders() {
        return repository.getAll();
    }
}
