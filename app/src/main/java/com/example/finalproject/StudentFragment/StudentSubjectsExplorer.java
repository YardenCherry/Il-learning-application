package com.example.finalproject.StudentFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.finalproject.R;
import com.example.finalproject.StudentActivity;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentSubjectsExplorer extends Fragment {

    View thisView;
    private LinearLayout subjects_Explorer;
    private String studentId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.student_subjects_fragment, container, false);
        studentId = LocalStorageService.getUser().get("id").toString();
        findViews();
        return thisView;
    }

    private void findViews() {
        subjects_Explorer = thisView.findViewById(R.id.student_new_subjects_list);
        getSubjects();
    }

    public void getSubjects(){
        FireStoreService.getListOfSubjects().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> result = queryDocumentSnapshots.getDocuments();
                HashMap<String, ArrayList<String>> subjectTeachers = new HashMap<>();

                for(DocumentSnapshot doc : result){
                    ArrayList<?> subjects = (ArrayList<?>) doc.get("subjects");
                    for(int i = 0; i < ((ArrayList<?>) doc.get("subjects")).size(); i++){
                        String s =  ((ArrayList<?>) doc.get("subjects")).get(i).toString();
                        if(subjectTeachers.containsKey(s)){
                            subjectTeachers.get(s).add(doc.getId());
                        }
                        else{
                            subjectTeachers.put(s, new ArrayList<String>());
                            subjectTeachers.get(s).add(doc.getId());
                        }
                    }
                }
                for(String key : subjectTeachers.keySet()){
                    View rowView = LayoutInflater.from(thisView.getContext()).inflate(R.layout.subject_row, null);
                    TextView subjectNameTextView = rowView.findViewById(R.id.subject_row_name);
                    subjectNameTextView.setText(key);
                    subjectNameTextView.setOnClickListener(v -> {
                        StudentActivity.switchToSubjectTeachers(subjectTeachers.get(key), key);
                    });
                    subjects_Explorer.addView(rowView);
                }

            }
        });
    }
}
