package com.example.finalproject.TeacherFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;

import java.util.ArrayList;
import java.util.Objects;

public class MySubjects extends Fragment {

    private Button addSubjectsBtn;
    View thisView;
    private LinearLayout subjects;
    String teacherID;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_subjects_view, container, false);
        teacherID = LocalStorageService.getUser().get("id").toString();
        initViews();
        refreshFromStorage();
        return thisView;
    }

    private void initViews(){
        addSubjectsBtn = thisView.findViewById(R.id.AddSubject);
        addSubjectsBtn.setOnClickListener(view -> showAddSubjectDialog());
        subjects = thisView.findViewById(R.id.teacher_subjects_list);
    }

    private void refreshFromStorage(){
        subjects.removeAllViews();
        FireStoreService.findUser(teacherID).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get("subjects") != null &&
                    documentSnapshot.get("subjects") instanceof ArrayList<?>) {
                for(int i = 0; i < ((ArrayList<?>) documentSnapshot.get("subjects")).size(); i++){
                    String s =  ((ArrayList<?>) documentSnapshot.get("subjects")).get(i).toString();
                    addSubjectToList(s);
                }
            }
        });
    }

    private void addSubjectToList(String subjectName){
        View rowView = LayoutInflater.from(thisView.getContext()).inflate(R.layout.subject_row, null);
        TextView subjectNameTextView = rowView.findViewById(R.id.subject_row_name);
        subjectNameTextView.setText(subjectName);
        subjects.addView(rowView);
    }


    private void showAddSubjectDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(thisView.getContext());
            builder.setTitle("Add Subject");

            final EditText input = new EditText(thisView.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String subjectName = input.getText().toString().trim();
                if (subjectName.equals("")) {
                    Toast.makeText(thisView.getContext(), "Subject name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                FireStoreService.addSubjectToTeacher(teacherID, subjectName)
                        .addOnSuccessListener(aVoid -> {
                            Objects.requireNonNull(FireStoreService.findUser(teacherID)).addOnSuccessListener(document -> {
                                LocalStorageService.updateUser(document);
                                Toast.makeText(getContext(), "Subject added successfully: " + subjectName, Toast.LENGTH_SHORT).show();
                                refreshFromStorage();
                            });

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to add subject: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
