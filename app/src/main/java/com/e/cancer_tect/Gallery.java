package com.e.cancer_tect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.io.IOException;

public class Gallery {

    private Activity activity;
    private Context context;
    private static final int REQUEST_GALLERY = 2;

    public Gallery(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void createGalleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
    }

}
