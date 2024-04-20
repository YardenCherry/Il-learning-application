package com.example.finalproject.StudentFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.finalproject.R;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.example.finalproject.model.Lesson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StudentLessons extends Fragment {
    View thisView;
    private LinearLayout studentLessonsLayout;
    private String studentId;


    private ArrayList<Lesson> studentLessons = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.user_lessons, container, false);
        studentId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }



    private void findViews() {
        studentLessonsLayout = thisView.findViewById(R.id.user_lessons_list);
        fetchLessons();

    }

    private void fetchLessons(){
        studentLessons.clear();
        FireStoreService.getStudentLessons(studentId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        studentLessons.add(lesson);
                }
                displayLessonList();
            }
        });
    }

    private void displayLessonList() {
        studentLessonsLayout.removeAllViewsInLayout();
//        studentLessonsLayout.removeAllViews();
        for(Lesson l : studentLessons){
            View cardView = LayoutInflater.from(thisView.getContext()).inflate(R.layout.lesson_card, null);
            MaterialTextView teacherName = cardView.findViewById(R.id.user_LBL_name);
            MaterialTextView subject = cardView.findViewById(R.id.subject_LBL_name);
            subject.setText(l.getSubject());
            MaterialTextView date = cardView.findViewById(R.id.lesson_date);
            date.setText(l.getDate());
            MaterialTextView time = cardView.findViewById(R.id.lesson_time);
            Button delete = cardView.findViewById(R.id.delete_button);
            delete.setOnClickListener(v-> deleteLesson(l.getLessonId(), l.getTeacherId(), studentId));
            MaterialTextView phoneNumber = cardView.findViewById(R.id.user_LBL_phone);
            String lessonTime = l.getStartTime()+" - "+ l.getEndTime();
            time.setText(lessonTime);
            FireStoreService.findUser(l.getTeacherId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    teacherName.setText("Teacher name: " +documentSnapshot.get("name").toString());
                    phoneNumber.setText("Teacher phone number: "+ documentSnapshot.get("phoneNumber").toString());
                }
            });
            studentLessonsLayout.addView(cardView);
        }

    }

    private void deleteLesson(String lessonId, String teacherId, String studentId) {
        FireStoreService.deleteLesson(lessonId, teacherId, studentId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Lesson deleted successfully", Toast.LENGTH_SHORT).show();
                            fetchLessons();
                        } else {
                            // Deletion failed, show error message
                            Toast.makeText(requireContext(), "Failed to delete lesson", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
