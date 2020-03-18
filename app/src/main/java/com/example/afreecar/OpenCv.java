package com.example.afreecar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

public class OpenCv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_cv);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(), "Open CV working", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Open CV not working", Toast.LENGTH_SHORT).show();
        }
    }
}
