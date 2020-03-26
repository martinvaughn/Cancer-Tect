package com.e.cancer_tect;

import android.content.Intent;
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

        Intent intent = getIntent();
        String prediction = intent.getStringExtra("analysis");
    }


    //display analysis -> System.out(prediction);
    void display() {
        // display anlysis;
    }
}
