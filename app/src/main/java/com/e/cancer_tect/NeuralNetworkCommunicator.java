package com.e.cancer_tect;

import android.app.Activity;

import android.graphics.Bitmap;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.DataType;

import org.tensorflow.lite.support.common.TensorOperator;

import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
//import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
//import org.tensorflow.lite.support.image.ops.Rot90Op;
//import org.tensorflow.lite.support.label.TensorLabel;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

import java.util.Map;


public class NeuralNetworkCommunicator implements Runnable {
    private Bitmap bitmap;
    private Interpreter tflite = null;
    private static final String MODEL_PATH = "final_cancer_model.tflite";
    private int DIMEN = 50;
    private Activity activity;
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    private String analysis;
    private TensorImage inputImage;
    private int imageSizeX;
    private int imageSizeY;


    public NeuralNetworkCommunicator(Bitmap bitmap, Activity activity) {
        this.bitmap = bitmap;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Log.d("NeuralNetClass", "Starting NeuralNet");
            tflite = new Interpreter(loadModelFile(this.activity), tfliteOptions);
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.test2);

            Log.d("NeuralNetClass", "Bitmap created.");
            Map<Integer, Object> output = new HashMap<>();
            output.put(0, new float[1][1]);

            bitmap = resizeBitmap(bitmap, DIMEN);
            int[] imageShape = tflite.getInputTensor(0).shape();
            imageSizeY = imageShape[1];
            imageSizeX = imageShape[2];
            DataType imageDataType = tflite.getInputTensor(0).dataType();

            inputImage = new TensorImage(imageDataType);
            //System.out.println(inputImage);
            inputImage = loadImage(bitmap);
            System.out.println(inputImage);
            Object[] inputs = {inputImage.getBuffer()};
            tflite.runForMultipleInputsOutputs(inputs, output);
            float[][] a = (float[][]) output.get(0) ;

            System.out.println(a[0][0]);
            if (a[0][0] == 0)
                analysis = "Malignant";
            else
                analysis = "Benign";
            System.out.println(analysis);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tflite.close();
        Log.d("NeuralNetClass", "NeuralNet Closed");
    }

    // SENG WORK HERE PLEASE

}