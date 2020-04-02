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
        String filename = intent.getStringExtra("img"); //Get filename from the intent.
        bitmap = null;

        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is); //Load local bitmap with bitmap from Main Activity.
            is.close();
            displayImage(bitmap); //Display bitmap to user as NeuralNet loads
            startNeuralNet(); //Begin Neural Network. Result will be returned with a runOnUIThread
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Display's the user Image back for confirmation of procedure.
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

    //Allows prediction to be set from a runOnUIThread
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    //Once Neural Network finishes, made the loading features invisible and the continue button visible.
    public void setVisibility() {
        ImageView loadingIcon = findViewById(R.id.loading);
        TextView loadingText = findViewById(R.id.loadingText);
        Button startP = findViewById(R.id.startP);
        loadingIcon.setVisibility(View.GONE); //Loading Icon now Invisible
        loadingText.setVisibility(View.GONE); //Loading Text now Invisible
        startP.setVisibility(View.VISIBLE); //Continue button now Visible
    }

    //Start activity to display to the user our prediction
    public void startPrediction(View view) {
        Intent intent = new Intent(this, PredictionActivity.class);
        intent.putExtra("Prediction", prediction);
        startActivity(intent); //Start the prediction activity when Continue is clicked.
    }
}


