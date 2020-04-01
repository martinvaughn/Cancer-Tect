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

    private Button button;
    private Button button2;
    private Bitmap bitmap = null;
    Camera camera;
    Gallery gallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.Button);
        button2 = findViewById(R.id.Button2);
        final Context context = getApplicationContext();
        camera = new Camera(this, context);
        gallery = new Gallery(this, context);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.saveImage(); //this was changed

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery.createGalleryIntent();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bitmap = camera.getPic();
            startAnalysis();
        }

        if(requestCode == 2 && resultCode == RESULT_OK) {
            openGallery(data);
            startAnalysis();
        }

        } catch (Exception ex) {
            Toast.makeText(this, "Error Retrieving Image.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void startAnalysis() {
            try {
                String filename = "bitmap.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                bitmap.recycle();

                Intent intent = new Intent(this, AnalysisActivity.class);
                intent.putExtra("img", filename);

                Log.d("MainActivity", "Starting Analysis Activity with the image");
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void launchDoctorActivity(View view)
    {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=cancer");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void options(View view){


    }

    public void openGallery(Intent data) {
        Uri filePath = data.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            Log.d("Main Activity: ", "Bitmap created: " + bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
