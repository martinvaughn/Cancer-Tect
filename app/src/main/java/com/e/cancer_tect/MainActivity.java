package com.e.cancer_tect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    Camera camera;
    Gallery gallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create buttons ready to be clicked
        Button button = findViewById(R.id.Button);
        Button button2 = findViewById(R.id.Button2);
        final Context context = getApplicationContext();

        //Create camera and gallery objects ready to be used.
        camera = new Camera(this, context);
        gallery = new Gallery(this);

        //Camera button action when clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.saveImage(); //Allow user to take an image.
            }
        });

        //Gallery button action when clicked
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.createGalleryIntent(); //Open users gallery
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
        super.onActivityResult(requestCode, resultCode, data);

        //If user took a picture, save the image to a local bitmap then start next activity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bitmap = camera.getPic();
            startAnalysis();
        }

        //If user selected a picture from gallery, save the Bitmap and start next activity.
        if(requestCode == 2 && resultCode == RESULT_OK) {
            saveBitmap(data);
            startAnalysis();
        }

        } catch (Exception ex) {
            Toast.makeText(this, "Error Retrieving Image.",
                    Toast.LENGTH_SHORT).show(); //Inform user if Photo creates an error.
        }
    }

    public void startAnalysis() {
            try {
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                //Compress bitmap and write to a local file to be sent with an intent.
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                bitmap.recycle();

                Intent intent = new Intent(this, AnalysisActivity.class);
                intent.putExtra("img", filename); //Put file into the intent

                Log.d("MainActivity", "Starting Analysis Activity with the image");
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void launchDoctorActivity(View view) {
        //Create URI with Dermatologist as a query
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=dermatologist");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        //Open users Google maps application
        startActivity(mapIntent);
    }

    public void saveBitmap(Intent data) {
        Uri filePath;
        filePath = data.getData();
        try {
            //Save bitmap as whichever photo the user selected.
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            Log.d("Main Activity: ", "Bitmap created: " + bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
