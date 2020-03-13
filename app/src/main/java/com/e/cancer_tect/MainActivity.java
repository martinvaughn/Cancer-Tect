package com.e.cancer_tect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private Bitmap bitmap;
    Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this here was to test to see if the camera would work.

        button = (Button) findViewById(R.id.Button);
        final Context context = getApplicationContext();
        final Intent intent = getIntent();
        camera = new Camera(this, context);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.saveImage();
                startAnalysis(v);
            }//onClick
        });//OnClickListener
    }

        public void startAnalysis(View v) {
            try {
                String filename = "bitmap.png";
                bitmap = camera.getPic(v);
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                bitmap.recycle();

                Intent intent = new Intent(this, AnalysisActivity.class);
                intent.putExtra("img", filename);

                Log.d("MainActivity", "Starting AnalysisActivity with the image");
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void launchDoctorActivity(View view)
    {
        Intent intent = new Intent(this, DoctorActivity.class);
        startActivity(intent);
    }

}
