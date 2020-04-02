package com.e.cancer_tect;

import android.app.Activity;
import android.content.Intent;

class Gallery {

    private Activity activity;
    private static final int REQUEST_GALLERY = 2;

    //Non-default constructor
    Gallery(Activity activity) {
        this.activity = activity;
    }

    //Create an intent to open user Gallery
    void createGalleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
    }

}
