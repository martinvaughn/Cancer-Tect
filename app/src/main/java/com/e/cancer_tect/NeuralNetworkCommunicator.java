package com.e.cancer_tect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class NeuralNetworkCommunicator implements Runnable {
    private Bitmap bitmap;
    private Interpreter tflite = null;
    private static final String MODEL_PATH = "final_cancer_model_2.tflite";
    private Activity activity;
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    private TensorImage inputImage;
    private int imageSizeX;
    private int imageSizeY;


    //Set bitmap and current activity as analysis activity.
    public NeuralNetworkCommunicator(Bitmap bitmap, Activity activity) {
        this.bitmap = bitmap;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Log.d("NeuralNetClass", "Starting NeuralNet");
            //Create a Tensorflow Interpreter.
            tflite = new Interpreter(loadModelFile(this.activity), tfliteOptions);

            //Create a Map object capable of receiving an output from the Interpreter.
            @SuppressLint("UseSparseArrays") Map<Integer, Object> output = new HashMap<>();
            //Create a map item with key "0".
            output.put(0, new float[1][1]);

            int DIMEN = 50; //Photo should be 50px by 50px.
            bitmap = resizeBitmap(bitmap, DIMEN); //Resize bitmap.

            //Receive the needed image shape (in pixels) from the Interpreter.
            int[] imageShape = tflite.getInputTensor(0).shape();
            imageSizeY = imageShape[1]; //50.
            imageSizeX = imageShape[2]; //50.

            //Retrieve the data type from the Interpreter.
            DataType imageDataType = tflite.getInputTensor(0).dataType();

            //Create a Tensorflow Image, then fill it with our Bitmap.
            inputImage = new TensorImage(imageDataType);
            inputImage = loadImage(bitmap);

            //Create an inputs object, readable by the Interpreter.
            Object[] inputs = {inputImage.getBuffer()};

            //Run the Tensorflow model through the Interpreter. Saves prediction to the output map's 0 key.
            tflite.runForMultipleInputsOutputs(inputs, output);

            //Retrieve analysis from the output.
            float[][] a = (float[][]) output.get(0);
            String analysis;
            if (a[0][0] == 0)
                analysis = "Malignant";
            else
                analysis = "Benign";
        } catch (Exception e) {
            e.printStackTrace();
        }

        tflite.close(); //close the interpreter after obtaining a prediction.
        Log.d("NeuralNetClass", "NeuralNet Closed");
    }


    //loads the Neural Network File.
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        Log.d("NeuralNetClass", "Starting LoadModel method.");

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());

        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }


    //Resize bitmap to desired dimensions.
    private Bitmap resizeBitmap(Bitmap image, int dimension) {
        return Bitmap.createScaledBitmap(image, dimension, dimension, true);
    }

    //Load a bitmap object into a TensorImage object.
    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImage.load(bitmap);

        //Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder().add(new ResizeWithCropOrPadOp(cropSize, cropSize)).add(new ResizeOp(imageSizeX, imageSizeY, ResizeMethod.NEAREST_NEIGHBOR)).add(getPreprocessNormalizeOp()).build();

        return imageProcessor.process(inputImage);

    }

    protected TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(127.5f, 127.5f);
    }
}