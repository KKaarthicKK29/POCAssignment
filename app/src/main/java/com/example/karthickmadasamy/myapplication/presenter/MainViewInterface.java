package com.example.karthickmadasamy.myapplication.presenter;

import com.example.karthickmadasamy.myapplication.models.FeederModel;

/**
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public interface MainViewInterface {
    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayFeeder(FeederModel newsResponse);
    void displayError(String s);
}

