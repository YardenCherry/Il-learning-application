package com.example.finalproject.TeacherFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.TeacherActivity;
import com.example.finalproject.control.LocalStorageService;

public class TeacherMenu extends Fragment {
    private View thisView;
    private Button mySubjects;
    private Button updateProfile;
    private Button teacherLessons;
    private Button myProfile;

    private String teacherId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_menu, container, false);
        teacherId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }

    private void findViews() {
        mySubjects = thisView.findViewById(R.id.teacherSubjectButton);
        mySubjects.setOnClickListener(view -> TeacherActivity.goToSubjects());
        updateProfile = thisView.findViewById(R.id.teacher_update_profile);
        updateProfile.setOnClickListener(view -> TeacherActivity.goToUpdateProfile());
        myProfile= thisView.findViewById(R.id.myTeacherProfile);
        myProfile.setOnClickListener(view->TeacherActivity.goToTeacherProfile());
        teacherLessons= thisView.findViewById(R.id.teacherLessonButton);
        teacherLessons.setOnClickListener(view->TeacherActivity.goToTeacherLessons());
    }




}
