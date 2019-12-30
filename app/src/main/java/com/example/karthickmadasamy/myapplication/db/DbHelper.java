package com.example.karthickmadasamy.myapplication.db;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Karthick.Madasamy on 12/27/2019.
 */

@Database(entities = { FeederEntity.class }, version = 3, exportSchema = false)
abstract  public class DbHelper  extends RoomDatabase {



    abstract public FeederDao getFeederDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    private static DbHelper instance;
    public static synchronized DbHelper getInstance(Context context){
        if(null==instance){
            instance = Room.databaseBuilder(context,DbHelper.class,"feeder_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
