package com.e.cancer_tect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

    }

    public void searchDoctors() {
        EditText et = findViewById(R.id.EnterZip);
        String location = et.getEditableText().toString();
        String url = "https://api.betterdoctor.com/2016-03-01/doctors?query=cancer&location=" + location + "&skip=0&limit=10&user_key=0704e8d97bccc14cbee4ace93ac1e0f4";
        URLConnection connection;
        Gson gson = new Gson();
        String charset = "UTF-8";

        try {
            connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset));
            String line = reader.readLine();
            System.out.println(line);
            //DoctorInfo doctorInfo = gson.fromJson(line, DoctorInfo.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.DoctorDisplay);
        List<String> list = new ArrayList<String>();

        //list.add(doctorInfo. something)

       // for (doctorInfo DI : )

    }
}
