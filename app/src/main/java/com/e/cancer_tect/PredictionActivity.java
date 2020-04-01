package com.e.cancer_tect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        //get analysis from the intent;
        Intent intent = getIntent();
        String prediction = intent.getStringExtra("Prediction");
        if (prediction != null) {
            if (prediction.equals("Benign")) {
                TextView noCancer = findViewById(R.id.noCancer);
                noCancer.setVisibility(View.VISIBLE);

                ImageView benign = findViewById(R.id.benign);
                benign.setVisibility(View.VISIBLE);
            } else {
                TextView cancer = findViewById(R.id.cancer);
                cancer.setVisibility(View.VISIBLE);

                ImageView malignant = findViewById(R.id.malignant);
                malignant.setVisibility(View.VISIBLE);
            }
        }

    }

    public void findDoctor(View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=cancer");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
