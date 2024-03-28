package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalproject.control.LocalStorageService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register, teacherSignIn, studentSignIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public void onClick(View v) {

    }
}