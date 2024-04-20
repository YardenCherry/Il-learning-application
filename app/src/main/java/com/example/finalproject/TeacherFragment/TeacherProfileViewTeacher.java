package com.example.finalproject.TeacherFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.finalproject.R;
import com.example.finalproject.control.LocalStorageService;
import com.google.android.material.textview.MaterialTextView;

public class TeacherProfileViewTeacher extends Fragment {
    private View thisView;
    private String teacherId;
    private MaterialTextView overView;
    private MaterialTextView price;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_profile_teacher_view, container, false);
        teacherId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }

    private void findViews() {
        overView= thisView.findViewById(R.id.overview_LBL);
        price= thisView.findViewById(R.id.price_LBL);
        displayDetails();
    }

    private void displayDetails() {
        String overViewText = "Nothing to show..";
        String cost = "100";
        if (LocalStorageService.getUser().get("About me")!= null)
            overViewText = LocalStorageService.getUser().get("About me").toString();
        if (LocalStorageService.getUser().get("Cost")!= null)
            cost = LocalStorageService.getUser().get("Cost").toString();
        overView.setText(overViewText);
        price.setText(cost);
    }

}
