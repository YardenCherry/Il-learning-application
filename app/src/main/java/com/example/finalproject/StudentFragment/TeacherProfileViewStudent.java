package com.example.finalproject.StudentFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.StudentActivity;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.example.finalproject.model.Lesson;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeacherProfileViewStudent extends Fragment {
    View thisView;
    private String studentId;
    private TextView teacherName;
    private TextView teacherOverview;
    private TextView lessonPrice;
    private Button selectDay;
    private MaterialTextView lesson_time;
    private Spinner selectHour;
    private Button setLesson;
    private String teacherId;
    private String selectedDay;
    private String selectedTime;
    private String selectedDate;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_profile_student_view, container, false);
        studentId = LocalStorageService.getUser().get("id").toString();
        teacherId = getArguments().get("teacher").toString();
        findViews();
        return thisView;
    }

    private void findViews() {
        teacherName = thisView.findViewById(R.id.teacherPage_LBL_name);
        teacherOverview = thisView.findViewById(R.id.teacher_LBL_overview);
        lessonPrice = thisView.findViewById(R.id.teacher_LBL_cost);
        selectDay = thisView.findViewById(R.id.chose_lesson_day);
        selectHour = thisView.findViewById(R.id.chose_lesson_hour_spinner);
        setLesson = thisView.findViewById(R.id.set_lesson_BTN);
        lesson_time = thisView.findViewById(R.id.lesson_time_details);
        displayTeacherDetails();
        selectDay.setOnClickListener(view -> showCalendar());
        setLesson.setOnClickListener(v -> setLessonAtLessons());
        selectHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                selectedTime = selectedItem;
                String displayText = selectedDay + ": " + selectedItem;
                lesson_time.setText(displayText);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setLessonAtLessons() {
        String startTime = selectedTime.split(" - ")[0];
        String endTime = selectedTime.split(" - ")[1];
        Lesson lesson = new Lesson(selectedDate,
                startTime,
                endTime,
                studentId,
                teacherId, getArguments().get("subject").toString());



        lessonAvailable(lesson);

    }

    private void displayTeacherDetails() {
        FireStoreService.findUser(teacherId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String overView = "Nothing to show..";
                String cost = "100";
                String name = documentSnapshot.get("name").toString();
                if (documentSnapshot.contains("About me"))
                    overView = documentSnapshot.get("About me").toString();
                if (documentSnapshot.contains("Cost"))
                    cost = documentSnapshot.get("Cost").toString();
                teacherName.setText(name);
                teacherOverview.setText(overView);
                lessonPrice.setText(cost);
            }
        });
    }
    private void showCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    // Create a new calendar instance for the selected date
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, monthOfYear, dayOfMonth);
                    selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    // Get the day of the week for the selected date
                    int dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                    // Convert the day of the week integer to a string representation
                    String[] daysOfWeek = new DateFormatSymbols().getWeekdays();
                    String dayOfWeekString = daysOfWeek[dayOfWeek];
                    Log.d("DAYOFWEEK", dayOfWeekString);

                    // Format the selected date
                    String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

                    // Set the selected date and day of the week in UI elements
                    selectDay.setText(date);
                    checkWorkHours(dayOfWeekString);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void checkWorkHours(String day) {
        selectedDay = day;
        FireStoreService.findUser(teacherId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String workingHours = documentSnapshot.get(day).toString();
                if (workingHours.equals("Not working")) {
                    Toast.makeText(requireContext(), "Teacher is not available on this day", Toast.LENGTH_SHORT).show();
                    return;
                }
                String startEnd[] = workingHours.split(" - ");
                String start = startEnd[0].split(":")[0];
                String end = startEnd[1].split(":")[0];
                ArrayList<String> hourChoices = new ArrayList<>();
                for (int i = Integer.parseInt(start); i < Integer.parseInt(end); i++) {
                    hourChoices.add(i + ":00 - " + (i + 1) + ":00");
                }

                // Create an ArrayAdapter using the hourChoices list
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hourChoices);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Set the ArrayAdapter to the selectHour spinner
                selectHour.setAdapter(adapter);

            }
        });
    }

    private void lessonAvailable(Lesson lesson) {
        FireStoreService.filterLessons(lesson.getDate(), lesson.getTeacherId(), lesson.getStudentId()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean available = true;
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {

                    if(doc.get("student").toString().equals(lesson.getStudentId()) ||
                    doc.get("teacher").toString().equals(lesson.getTeacherId())){
                        if(lesson.getStartTime().equals(doc.get("start").toString())
                        || lesson.getEndTime().equals(doc.get("end").toString())){
                            available = false;
                            Toast.makeText(requireContext(), "Lesson colides with another lesson, try a different time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if(available){
                            FireStoreService.addLesson(lesson);
                            StudentActivity.showStudentMenu();
                }
            }
        });
    }

}

