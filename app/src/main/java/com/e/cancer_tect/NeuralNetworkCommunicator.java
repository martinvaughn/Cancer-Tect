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


    public NeuralNetworkCommunicator(Bitmap bitmap, Activity activity) {
        this.bitmap = bitmap;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Log.d("NeuralNetClass", "Starting NeuralNet");
            tflite = new Interpreter(loadModelFile(this.activity), tfliteOptions);

            Log.d("NeuralNetClass", "Bitmap created.");
            @SuppressLint("UseSparseArrays") Map<Integer, Object> output = new HashMap<>();
            output.put(0, new float[1][1]);

            int DIMEN = 50;
            bitmap = resizeBitmap(bitmap, DIMEN);
            int[] imageShape = tflite.getInputTensor(0).shape();
            imageSizeY = imageShape[1];
            imageSizeX = imageShape[2];
            DataType imageDataType = tflite.getInputTensor(0).dataType();

            inputImage = new TensorImage(imageDataType);
            inputImage = loadImage(bitmap);


            Object[] inputs = {inputImage.getBuffer()};
            tflite.runForMultipleInputsOutputs(inputs, output);
            float[][] a = (float[][]) output.get(0);

            String analysis;
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


    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        Log.d("NeuralNetClass", "Starting LoadModel");

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);

        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());

        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }


    private Bitmap resizeBitmap(Bitmap image, int dimension) {
        return Bitmap.createScaledBitmap(image, dimension, dimension, true);
    }


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