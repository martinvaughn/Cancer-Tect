package com.e.cancer_tect;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        //get analysis from the intent;
        //Intent intent = getIntent();
        //String prediction = intent.getStringExtra("analysis");
    }


    //display analysis -> System.out(prediction);
}
