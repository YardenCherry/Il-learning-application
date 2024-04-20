package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.StudentFragment.StudentMenu;
import com.example.finalproject.StudentFragment.SubjectTeachersFragment;
import com.example.finalproject.StudentFragment.TeacherProfileViewStudent;
import com.example.finalproject.control.LocalStorageService;
import java.util.ArrayList;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {

    private TextView studentName;
    private Map<String, Object> userDetails;
    private ImageButton logOut;

    private static StudentActivity instance;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        userDetails = LocalStorageService.getInstance(StudentActivity.this).getUser();
        findViews();
        instance = this;
        initializeBackButtonCallback();

    }

    private void findViews() {
        logOut = findViewById(R.id.logoutButton);
        logOut.setOnClickListener(v->logOutUser());
        studentName = findViewById(R.id.studentNameLabel);
        studentName.setText(userDetails.get("name").toString());
        StudentMenu studentmenu = new StudentMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, studentmenu)
                .commit();
    }

    private void logOutUser() {
        LocalStorageService.userLogOut();
        Intent intent = new Intent(StudentActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public static void switchToSubjectTeachers(ArrayList<String> teachers, String subject){
        SubjectTeachersFragment subjectTeachersFragment = new SubjectTeachersFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("teachers", teachers);
        bundle.putString("subject", subject);
        subjectTeachersFragment.setArguments(bundle);
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, subjectTeachersFragment)
                .commit();
    }

    public static void switchToTeacherPage(String teacherId, String subject){
        TeacherProfileViewStudent teacherProfile = new TeacherProfileViewStudent();
        Bundle bundle = new Bundle();
        bundle.putString("teacher", teacherId);
        bundle.putString("subject", subject);
        teacherProfile.setArguments(bundle);
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, teacherProfile)
                .commit();
    }

    public static void showStudentMenu(){
        StudentMenu studentMenu = new StudentMenu();
        instance.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, studentMenu)
                .commit();
    }
    private void initializeBackButtonCallback() {
        // Create a callback for handling back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle back button press here
                showStudentMenu(); // Show student menu when back button is pressed
            }
        };

        // Add the callback to the activity's OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


}
