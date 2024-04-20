package com.example.finalproject.StudentFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import com.example.finalproject.R;
import com.example.finalproject.StudentActivity;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class SubjectTeachersFragment extends Fragment {
    View thisView;
    private LinearLayout teachers_for_subject;
    private String studentId;

    private ArrayList<String> teachers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.subject_teachers_fragment, container, false);
        studentId = LocalStorageService.getUser().get("id").toString();
        teachers = getArguments().getStringArrayList("teachers");
        findViews();
        return thisView;
    }

    private void findViews() {
        teachers_for_subject = thisView.findViewById(R.id.subject_teachers_list);
        displayTeachers();
    }
    private void displayTeachers(){
        for (String teacher : teachers){
            FireStoreService.findUser(teacher).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    createTeacherCard(documentSnapshot);
                }
            });
        }
    }

    private void createTeacherCard(DocumentSnapshot documentSnapshot){
        String aboutMe = "Nothing to show...";
        String cost = "100";
        String name = documentSnapshot.get("name").toString();
        int age = Integer.parseInt(documentSnapshot.get("age").toString());
        String email = documentSnapshot.get("email").toString();
        String phoneNumber = documentSnapshot.get("phoneNumber").toString();
        if(documentSnapshot.contains("About me"))
            aboutMe = documentSnapshot.get("About me").toString();
        if(documentSnapshot.contains("Cost"))
            cost =documentSnapshot.get("Cost").toString();
        View cardView = LayoutInflater.from(thisView.getContext()).inflate(R.layout.teacher_card, null);
        MaterialTextView teacherName = cardView.findViewById(R.id.teacher_LBL_name);
        teacherName.setText(name);
        MaterialTextView teacherAge= cardView.findViewById(R.id.teacher_LBL_age);
        teacherAge.setText(String.valueOf(age));
        MaterialTextView teacherEmail= cardView.findViewById(R.id.teacher_LBL_email);
        teacherEmail.setText(email);
        MaterialTextView teacherPhoneNumber= cardView.findViewById(R.id.teacher_LBL_phoneNumber);
        teacherPhoneNumber.setText(phoneNumber);
        MaterialTextView teacherOverview= cardView.findViewById(R.id.teacher_LBL_overview);
        teacherOverview.setText(aboutMe);
        MaterialTextView lessonCost = cardView.findViewById(R.id.Lesson_cost);
        lessonCost.setText(cost);
        ShapeableImageView teacherPicture = cardView.findViewById(R.id.teacher_IMG_poster);
        teacherPicture.setImageResource(R.drawable.teacher_picture);
        cardView.setOnClickListener(v-> StudentActivity.switchToTeacherPage(documentSnapshot.getId(), getArguments().get("subject").toString()));
        teachers_for_subject.addView(cardView);
    }
}
