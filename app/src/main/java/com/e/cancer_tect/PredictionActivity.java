package com.e.cancer_tect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    private String prediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        //get analysis from the intent;
        Intent intent = getIntent();
        prediction = intent.getStringExtra("analysis");

        if (prediction == "Benign") {
            TextView noCancer = (TextView)findViewById(R.id.noCancer);
            noCancer.setVisibility(View.VISIBLE);
        } else {
            TextView cancer = (TextView)findViewById(R.id.cancer);
            cancer.setVisibility(View.VISIBLE);
        }

        Button doctor = findViewById(R.id.doctor);
        doctor.setVisibility(View.VISIBLE);

    }
    //display analysis -> System.out(prediction);
}
