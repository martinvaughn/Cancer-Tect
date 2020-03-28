package com.e.cancer_tect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;


public class AnalysisActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private String prediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Intent intent = getIntent();
        bitmap = null;

        String filename = intent.getStringExtra("img");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            displayImage(bitmap);
            startNeuralNet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayImage(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.skinImage);
        imageView.setImageBitmap(bitmap);
    }

    //Run the CNN on a new Thread.
    public void startNeuralNet() {
        NeuralNetworkCommunicator CNN = new NeuralNetworkCommunicator(bitmap, this);
        Log.d("Analysis Activity", "Starting CNN Thread");
        Thread t = new Thread(CNN);
        t.start();
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setVisibility() {
        // change loading and button loading is not visible and button is visible
        ImageView loadingIcon = findViewById(R.id.loading);
        TextView loadingText = findViewById(R.id.loadingText);
        Button startP = findViewById(R.id.startP);
        loadingIcon.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        startP.setVisibility(View.VISIBLE);
    }

    public void startPrediction(View view) {
        Intent intent = new Intent(this, PredictionActivity.class);
        intent.putExtra("Prediction", prediction);
        startActivity(intent);
    }

}