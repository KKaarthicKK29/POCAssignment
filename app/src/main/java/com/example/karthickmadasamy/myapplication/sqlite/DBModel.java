package com.example.karthickmadasamy.myapplication.sqlite;

import com.example.karthickmadasamy.myapplication.models.Rows;

import java.util.List;

/**
 * Created by Karthick.Madasamy on 12/8/2019.
 */

public class DBModel {
    private int id;

    public String getFeederRows() {
        return feederRows;
    }

    public void setFeederRows(String feederRows) {
        this.feederRows = feederRows;
    }

    private String feederRows;

    public List<Rows> getFeederModel() {
        return feederModel;
    }

    public void setFeederModel(List<Rows> feederModel) {
        this.feederModel = feederModel;
    }

    private List<Rows> feederModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;


    public DBModel(List<Rows> feederModel,String title)
    {
        this.feederModel=feederModel;
        this.title=title;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
