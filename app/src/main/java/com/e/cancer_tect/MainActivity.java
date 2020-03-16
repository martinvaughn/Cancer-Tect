package com.e.cancer_tect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this here was to test to see if the camera would work.

        button = (Button) findViewById(R.id.Button);
        imageView = (ImageView) findViewById(R.id.Image_view);
        final Context context = getApplicationContext();
        final Intent intent = getIntent();
        camera = new Camera(this, context);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.saveImage();
            }//onClick
        });//OnClickListener

        Bitmap bitmap = camera.getPic();

    }

}
