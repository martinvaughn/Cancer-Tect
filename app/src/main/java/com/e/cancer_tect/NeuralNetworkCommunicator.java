package com.e.cancer_tect;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class NeuralNetworkCommunicator {
    private Interpreter tflite = null;
    private static final String MODEL_PATH = "v3_cancer_model.tfLite";
    private Activity activity;
    private Bitmap bitmap;
    private int DIMEN = 50;

    public NeuralNetworkCommunicator(Activity activity, Bitmap bitmap) {
        this.activity = activity;
        this.bitmap = bitmap;
        runModel();
    }

    public void runModel() {
        try {
            tflite = new Interpreter(loadModelFile(this.activity));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = resizeBitmap(bitmap, DIMEN);
        //tflite.getOutputTensor;
    }

    private Bitmap resizeBitmap(Bitmap image , int dimension ) {
        return Bitmap.createScaledBitmap( image , dimension , dimension , true ) ;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        //////STORE IN APP ASSETS
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
        }

    }
    //print(interpreter.get_input_details()[0]['dtype'])

            // Print output shape and type
    //print(interpreter.get_output_details()[0]['shape'])
    //print(interpreter.get_output_details()[0]['dtype'])

