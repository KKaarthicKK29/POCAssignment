package com.example.karthickmadasamy.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Karthick.Madasamy on 12/27/2019.
 */
@Entity(tableName = "feeder_table")
public class FeederEntity {

    //@PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @PrimaryKey
    private String title;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_href() {
        return imageHref;
    }



    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    private String imageHref;
}
