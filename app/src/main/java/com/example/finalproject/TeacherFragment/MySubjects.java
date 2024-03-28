package com.example.finalproject.TeacherFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MySubjects extends Fragment {

    private Button addSubjectsBtn;
    View thisView;
    private LinearLayout subjects;
    String teacherID;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.teacher_subjects_view, container, false);
        if(getArguments() != null)
            teacherID = getArguments().getString("teacherID");
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
            Log.d("<DOCUMENTSNAPSHOT>", documentSnapshot.toString());
            if (documentSnapshot.get("subjects") instanceof ArrayList<?>) {
                ArrayList<?> subjectsList = (ArrayList<?>) documentSnapshot.get("subjects");
                Log.d("<SUBJECTSSTRING>", documentSnapshot.get("subjects").toString());
//                String[] subjectStrings = documentSnapshot.get("subjects").toString().split(", ");
//                for(int i = 0; i < subjectStrings.length; i++){
//                    addSubjectToList(subjectStrings[i]);
//                }
                Toast.makeText(thisView.getContext(), ((ArrayList<?>) documentSnapshot.get("subjects")).get(0).toString(), Toast.LENGTH_SHORT);
                for(int i = 0; i < ((ArrayList<?>) documentSnapshot.get("subjects")).size(); i++){

//                    addSubjectToList(subjectsList.get(i).toString());

//                    String subName = ((ArrayList<?>) documentSnapshot.get("subjects")).get(i).toString();
//                    Log.d("SUBASSTRING", ((ArrayList<?>) documentSnapshot.get("subjects")).get(i).toString());
//                    addSubjectToList(subName);

                }
                if(subjectsList != null){
                    if(subjectsList.isEmpty()){
                        for(int i = 0; i < subjectsList.size(); i++){
                            addSubjectToList(subjectsList.get(i).toString());
                        }
                    } else{
//                        if(subjectsList.get(0) instanceof String)
//                            bundle.putStringArrayList("subjects", (ArrayList<String>) subjectsList);
//                          Toast.makeText(thisView.getContext(), "HERE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void addSubjectToList(String subjectName){
        @SuppressLint("ResourceType") View rowView = LayoutInflater.from(thisView.getContext()).inflate(R.id.subject_row, null);
        TextView subjectNameTextView = rowView.findViewById(R.id.subject_row_name);
        subjectNameTextView.setText(subjectName);
        Toast.makeText(thisView.getContext(), subjectName, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "Subject added successfully: " + subjectName, Toast.LENGTH_SHORT).show();
                            LocalStorageService.updateUser("");
                            refreshFromStorage();
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
