package com.e.cancer_tect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Bitmap bitmap = null;
    Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.Button);
        final Context context = getApplicationContext();
        camera = new Camera(this, context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.saveImage();
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
        Intent intent = new Intent(this, DoctorActivity.class);
        startActivity(intent);
    }

}
