package com.example.finalproject;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.finalproject.TeacherFragment.MySubjects;
import com.example.finalproject.TeacherFragment.TeacherLessons;
import com.example.finalproject.TeacherFragment.TeacherMenu;
import com.example.finalproject.TeacherFragment.TeacherEditProfileFragment;
import com.example.finalproject.TeacherFragment.TeacherProfileViewTeacher;
import com.example.finalproject.control.LocalStorageService;
import java.util.Map;

public class TeacherActivity extends AppCompatActivity {

    private String teacherId = "1234";
    private TextView teacherName;
    private Map<String, Object> userDetails;
    private static TeacherActivity instance;
    private ImageButton logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        userDetails = LocalStorageService.getInstance(TeacherActivity.this).getUser();
        instance = this;
        findViews();

        initializeBackButtonCallback();

    }


    private void findViews() {
        logOut = findViewById(R.id.logoutButton);
        logOut.setOnClickListener(v->logOutUser());
        teacherName = findViewById(R.id.teacherNameLabel);
        teacherName.setText(userDetails.get("name").toString());
        TeacherMenu teacherMenu = new TeacherMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, teacherMenu)
                .commit();

    }

    public static void goToSubjects(){
        MySubjects subjects = new MySubjects();

        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, subjects)
                .commit();
    }

    public static void goToUpdateProfile() {
        TeacherEditProfileFragment profile= new TeacherEditProfileFragment();
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, profile)
                .commit();

    }

    public static void goToTeacherLessons() {
        TeacherLessons lessons= new TeacherLessons();
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, lessons)
                .commit();

    }


    public static void goToTeacherMenu(){
        TeacherMenu teacherMenu = new TeacherMenu();
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, teacherMenu)
                .commit();
    }
    public static void goToTeacherProfile(){
        TeacherProfileViewTeacher teacherProfile= new TeacherProfileViewTeacher();
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, teacherProfile)
                .commit();
    }

    private void logOutUser() {
        LocalStorageService.userLogOut();
        Intent intent = new Intent(TeacherActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void initializeBackButtonCallback() {
        // Create a callback for handling back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press here
                goToTeacherMenu(); // Show student menu when back button is pressed
            }
        };

        // Add the callback to the activity's OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


}