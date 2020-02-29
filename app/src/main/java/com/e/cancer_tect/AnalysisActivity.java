package com.e.cancer_tect;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class AnalysisActivity extends AppCompatActivity {
    private NeuralNetworkCommunicator CNN;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        startNeuralNet();
    }

    public void startNeuralNet() {
        CNN = new NeuralNetworkCommunicator(AnalysisActivity.this, bitmap);
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