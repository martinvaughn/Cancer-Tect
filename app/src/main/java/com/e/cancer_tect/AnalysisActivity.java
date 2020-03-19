package com.e.cancer_tect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;


public class AnalysisActivity extends AppCompatActivity {
    private NeuralNetworkCommunicator CNN;
    private Bitmap bitmap;
    private String analysis;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Intent intent = getIntent();
        bitmap = null;

        String filename = getIntent().getStringExtra("img");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        displayImage();

        Log.d("Analysis Activity", "Starting Delay 4 Seconds");
        handler.postDelayed(new Runnable() {
            public void run() {
                startNeuralNet();
            }
        }, 4000);
    }

    public void displayImage() {

    }

    //Run the CNN on a new Thread.
    public void startNeuralNet() {
        CNN = new NeuralNetworkCommunicator(bitmap, this);
        Log.d("Analysis Activity", "Starting CNN Thread");
        Thread t = new Thread(CNN);
        t.start();
    }
}


/*************************************
 Bitmap bitmap = getYourInputImage();
 bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

 int batchNum = 0;
 float[][][][] input = new float[1][224][224][3];
 for (int x = 0; x < 224; x++) {
 for (int y = 0; y < 224; y++) {
 int pixel = bitmap.getPixel(x, y);
 // Normalize channel values to [-1.0, 1.0]. This requirement varies by
 // model. For example, some models might require values to be normalized
 // to the range [0.0, 1.0] instead.
 input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
 input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
 input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
 *********************************************/