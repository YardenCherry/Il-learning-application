package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;

public class MenuActivity extends AppCompatActivity {
    private Button register, teacherSignIn, studentSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FireStoreService.initService();
        LocalStorageService.initService(MenuActivity.this);
        findViews();

        register.setOnClickListener(view -> goToRegister());
        teacherSignIn.setOnClickListener(view-> goToSignIn());
        studentSignIn.setOnClickListener(view-> goToSignIn());
    }
    private void findViews() {
        register = findViewById(R.id.menu_BTN_register);
        teacherSignIn = findViewById(R.id.menu_BTN_loginTeacher);
        studentSignIn = findViewById(R.id.ment_BTN_loginStudent);
    }

    public void goToRegister(){
        Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToSignIn(){
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
