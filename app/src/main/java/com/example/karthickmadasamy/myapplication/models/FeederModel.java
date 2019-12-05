package com.example.karthickmadasamy.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model would only be the gateway to the domain layer or business logic.
 * FeederModel which helps to fetch the respective node from the api
 * Title is the primary node which has the secondary node called rows
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class FeederModel {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("rows")
    @Expose
    private List<Rows> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

}

