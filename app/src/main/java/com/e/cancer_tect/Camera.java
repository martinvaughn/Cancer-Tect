package com.e.cancer_tect;

/*
* Camera class
*
* This class will open the camera. Have the user take a picture then save it to a private folder.
*
* Author : Wyatt Nelson
*
* TODO:
*
*  1. Under res make new directory called "xml"
*  2. in the new directory, make new xml called "file_paths.xml"
*  3. paste " <?xml version="1.0" encoding="utf-8"?>
              <paths>
                 <external-files-path name="my_images" path="." />
              </paths>
*  4. In "AndroidManifest.xml" paste: " <uses-feature android:name="android.hardware.camera"
                                         android:required="true" />
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission> "
*     above "application" tag.
*  5. While in "AndroidManifest.xml" paste "  <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.example.android.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"></meta-data>
        </provider>"
*     into the "application" tag.
*
* */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath;
    private Activity activity;
    private Context context;


    public Camera(Activity ac, Context con){
        activity = ac;
        context = con;
    }


    /*
     * saveImage
     *
     * This will
     * */
    public void saveImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("onClick", "Error occurred while creating the File");
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.e.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }//if
        }//if
    }//saveImage

    /*
     * createImageFile()
     *
     * Returns a File.
     * this will create the file for the image. It will create a private file so only the
     * application will be able to access the photo.
     *
     *
     * */
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        //todo WHAT WON'T THIS WORK
        //File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    /*
     * getPic
     * This will return the Picture just taken by the camera and display it
     *
     * */

    public Bitmap getPic() {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW, photoH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        return bitmap;
    }



}
