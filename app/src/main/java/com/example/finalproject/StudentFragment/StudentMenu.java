package com.example.finalproject.StudentFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.control.LocalStorageService;

public class StudentMenu extends Fragment {

    private View thisView;
    private Button newSubjects;
    private Button myLesson;
    private String studentId;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.student_menu, container, false);
        studentId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }

    private void findViews() {
        newSubjects = thisView.findViewById(R.id.myNewSubjectButton);
        newSubjects.setOnClickListener(view -> goToNewSubjects());
        myLesson= thisView.findViewById(R.id.studentMyLesson);
        myLesson.setOnClickListener(view-> goToMyLessons());
    }

    private void goToMyLessons() {
        StudentLessons studentLessons = new StudentLessons();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, studentLessons)
                .commit();
    }

    private void goToNewSubjects() {
        StudentSubjectsExplorer subjectsExplorer = new StudentSubjectsExplorer();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmant_content, subjectsExplorer)
                .commit();

    }

}
