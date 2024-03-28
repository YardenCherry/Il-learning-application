package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.TeacherFragment.MySubjects;
import com.example.finalproject.control.LocalStorageService;

import java.util.ArrayList;
import java.util.Map;

public class TeacherActivity extends AppCompatActivity {

    private Button subjects;
    private String teacherId = "1234";
    private TextView teacherName;
    private Map<String, Object> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        userDetails = LocalStorageService.getInstance(TeacherActivity.this).getUser();
        findViews();
    }

    @SuppressWarnings("unchecked")
    private Bundle createFragmentBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("teacherID", userDetails.get("id").toString());
        if (userDetails.get("subjects") instanceof ArrayList<?>) {
            ArrayList<?> subjectsList = (ArrayList<?>) userDetails.get("subjects");
            if(subjectsList != null){
                if(subjectsList.isEmpty()){
                    bundle.putStringArrayList("subjects", new ArrayList<>());
                } else{
                    if(subjectsList.get(0) instanceof String)
                        bundle.putStringArrayList("subjects", (ArrayList<String>) subjectsList);
                }
            }
        }
        return bundle;
    }



    private void goToSubjects(){
        MySubjects subjects = new MySubjects();
        subjects.setArguments(createFragmentBundle());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmant_content, subjects)
                .commit();
    }

    private void findViews() {
        teacherName = findViewById(R.id.teacherNameLabel);
        teacherName.setText(userDetails.get("name").toString());
        subjects = findViewById(R.id.mySubjectButton);
        subjects.setOnClickListener(view -> goToSubjects());

    }

}