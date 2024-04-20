package com.example.finalproject.TeacherFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.TeacherActivity;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class TeacherEditProfileFragment extends Fragment {
    private EditText editTextAboutMe;
    private View thisView;
    private EditText editCost;
    private Button buttonSubmit;
    private Button sundayBtn;
    private Button mondayBtn;
    private Button tuesdayBtn;
    private Button wednesdayBtn;
    private Button thursdayBtn;
    private Button fridayBtn;
    private Button saturdayBtn;

    private String teacherId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_edit_profile_fragment, container, false);
        teacherId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }


    private void toggleWorkDay(Button button) {
        String buttonText = button.getText().toString();
        String[] buttonParts = buttonText.split(": "); // Split button text into day and working hours
        String dayOfWeek = buttonParts[0]; // Extract day of the week
        String currentStatus = buttonParts[1]; // Extract current working status

        String newStatus;
        if (currentStatus.equals("Not working")) {
            newStatus = "8:00 - 17:00"; // Set working hours
        } else {
            newStatus = "Not working"; // Set as not working
        }
        // Update the button text
        button.setText(dayOfWeek + ": " + newStatus);
        // Update the working hours in Firestore
        FireStoreService.updateDays(teacherId, dayOfWeek, newStatus);
    }


    private void findViews() {
        editTextAboutMe = thisView.findViewById(R.id.editTextAboutMe);
        buttonSubmit = thisView.findViewById(R.id.buttonSubmit);
        editCost= thisView.findViewById(R.id.cost_LBL);
        sundayBtn = thisView.findViewById(R.id.Sunday_hours);
        mondayBtn = thisView.findViewById(R.id.Monday_hours);
        tuesdayBtn = thisView.findViewById(R.id.Tuesday_hours);
        wednesdayBtn = thisView.findViewById(R.id.Wednesday_hours);
        thursdayBtn = thisView.findViewById(R.id.Thursday_hours);
        fridayBtn = thisView.findViewById(R.id.Friday_hours);
        saturdayBtn = thisView.findViewById(R.id.Saturday_hours);
        updateWeekDayButtons();
        sundayBtn.setOnClickListener(v -> toggleWorkDay(sundayBtn));
        mondayBtn.setOnClickListener(v -> toggleWorkDay(mondayBtn));
        tuesdayBtn.setOnClickListener(v -> toggleWorkDay(tuesdayBtn));
        wednesdayBtn.setOnClickListener(v -> toggleWorkDay(wednesdayBtn));
        thursdayBtn.setOnClickListener(v -> toggleWorkDay(thursdayBtn));
        fridayBtn.setOnClickListener(v -> toggleWorkDay(fridayBtn));
        saturdayBtn.setOnClickListener(v -> toggleWorkDay(saturdayBtn));
        buttonSubmit.setOnClickListener(v-> submitProfile());
    }
    private void updateWeekDayButtons() {
        FireStoreService.findUser(teacherId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve the values from Firestore document and set them to corresponding buttons
                    sundayBtn.setText("Sunday: "+documentSnapshot.getString("Sunday"));
                    mondayBtn.setText("Monday: "+documentSnapshot.getString("Monday"));
                    tuesdayBtn.setText("Tuesday: "+documentSnapshot.getString("Tuesday"));
                    wednesdayBtn.setText("Wednesday: "+documentSnapshot.getString("Wednesday"));
                    thursdayBtn.setText("Thursday: "+documentSnapshot.getString("Thursday"));
                    fridayBtn.setText("Friday: "+documentSnapshot.getString("Friday"));
                    saturdayBtn.setText("Saturday: "+documentSnapshot.getString("Saturday"));
                }
            }
        });
    }


    private void submitProfile() {
        String aboutMe = editTextAboutMe.getText().toString();
        String cost = editCost.getText().toString();
        FireStoreService.updateAboutTeacher(teacherId,aboutMe);
        FireStoreService.updateLessonCost(teacherId, cost);
        TeacherActivity.goToTeacherMenu();
    }
}
