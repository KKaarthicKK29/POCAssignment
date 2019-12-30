package com.example.karthickmadasamy.myapplication.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Karthick.Madasamy on 12/27/2019.
 */

@Dao
public interface FeederDao {
    @Insert(onConflict = REPLACE)
    public void  insert(FeederEntity entity);

    @Insert(onConflict = REPLACE)
    public void  insertAll(List<FeederEntity> entity);

    @Query("DELETE FROM feeder_table")
    public void deleteAll();

    @Query("select * FROM feeder_table")
    public LiveData<List<FeederEntity>> getAll();

}
