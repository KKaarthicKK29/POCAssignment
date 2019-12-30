package com.example.karthickmadasamy.myapplication.repo;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.karthickmadasamy.myapplication.db.DbHelper;
import com.example.karthickmadasamy.myapplication.db.FeederDao;
import com.example.karthickmadasamy.myapplication.db.FeederEntity;

import java.util.List;

import kotlin.Unit;

/**
 * Created by Karthick.Madasamy on 12/27/2019.
 */

public class FeederRepository {

    DbHelper dbHelper;
    private  LiveData<List<FeederEntity>> feeders;

    public FeederRepository(Context context){
        dbHelper = DbHelper.getInstance(context);
    }


    public void insert(FeederEntity entity) {
         new InsertNoteAsyncTask(dbHelper.getFeederDao()).execute(entity);
    }

    public void insertAll(List<FeederEntity> entities) {
        new InsertAllNoteAsyncTask(dbHelper.getFeederDao()).execute(entities);
    }

    private class InsertNoteAsyncTask extends AsyncTask<FeederEntity, Unit, Unit> {
        private FeederDao dao;
        public InsertNoteAsyncTask(FeederDao feederDao){
            this.dao = feederDao;
        }
        @Override
        protected Unit doInBackground(FeederEntity... feederDaos) {
            dao.insert(feederDaos[0]);
            return null;
        }

    }

    private class InsertAllNoteAsyncTask extends AsyncTask<List<FeederEntity>, Unit, Unit> {
        private FeederDao dao;
        public InsertAllNoteAsyncTask(FeederDao feederDao){
            this.dao = feederDao;
        }
        @Override
        protected Unit doInBackground(List<FeederEntity>... feederDaos) {
            dao.insertAll(feederDaos[0]);
            return null;
        }

    }

    public LiveData<List<FeederEntity>> getAll() {
        return dbHelper.getFeederDao().getAll();
    }

}
