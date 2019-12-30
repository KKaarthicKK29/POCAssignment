package com.example.karthickmadasamy.myapplication.view.activity;

import android.os.Bundle;

import com.example.karthickmadasamy.myapplication.R;
import com.example.karthickmadasamy.myapplication.view.fragments.FeederFragment;

/**
 * This is a Main class for our app,its redirect to feeder fragment which renders UI
 * Activity which can have multiple fragments
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeederFragment()).commit();
    }
}
