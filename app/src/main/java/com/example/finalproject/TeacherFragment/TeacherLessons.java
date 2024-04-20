package com.example.finalproject.TeacherFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.example.finalproject.model.Lesson;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TeacherLessons extends Fragment {
    private View thisView;
    private LinearLayout teacherLessonsLayout;
    private String teacherId;


    private ArrayList<Lesson> teacherLessons = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.user_lessons, container, false);
        teacherId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }



    private void findViews() {
        teacherLessonsLayout = thisView.findViewById(R.id.user_lessons_list);
        fetchLessonsToTeacher();

    }

    private void fetchLessonsToTeacher(){
        FireStoreService.getTeacherLessons(teacherId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    Lesson lesson = new Lesson(
                            doc.get("date").toString(),
                            doc.get("start").toString(),
                            doc.get("end").toString(),
                            doc.get("student").toString(),
                            doc.get("teacher").toString(),
                            doc.get("subject").toString(),
                            doc.getId()
                    );
                    Calendar cal = Calendar.getInstance();
                    int day = Integer.parseInt(lesson.getDate().split("/")[0]);
                    int month = Integer.parseInt(lesson.getDate().split("/")[1]) - 1;
                    int year = Integer.parseInt(lesson.getDate().split("/")[2]);
                    cal.set(year, month, day);

                    Date lessonDate = cal.getTime();

                    if(lessonDate.after(new Date()))
                        teacherLessons.add(lesson);
                }
                displayLessonList();
            }
        });
    }

    private void displayLessonList() {

        teacherLessonsLayout.removeAllViews();
        for(Lesson l : teacherLessons){
            View cardView = LayoutInflater.from(thisView.getContext()).inflate(R.layout.lesson_card, null);
            MaterialTextView studentName = cardView.findViewById(R.id.user_LBL_name);
            MaterialTextView subject = cardView.findViewById(R.id.subject_LBL_name);
            subject.setText(l.getSubject());
            MaterialTextView date = cardView.findViewById(R.id.lesson_date);
            date.setText(l.getDate());
            MaterialTextView time = cardView.findViewById(R.id.lesson_time);
            String lessonTime = l.getStartTime()+" - "+ l.getEndTime();
            time.setText(lessonTime);
            MaterialTextView phoneNumber = cardView.findViewById(R.id.user_LBL_phone);
            Button delete= cardView.findViewById(R.id.delete_button);
            delete.setVisibility(View.INVISIBLE);
            FireStoreService.findUser(l.getStudentId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    studentName.setText("Student name: " +documentSnapshot.get("name").toString());
                    phoneNumber.setText("Student phone number: " + documentSnapshot.get("phoneNumber").toString());
                }
            });
            teacherLessonsLayout.addView(cardView);
        }

    }
}
