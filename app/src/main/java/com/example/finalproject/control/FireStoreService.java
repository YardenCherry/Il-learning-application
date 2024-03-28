package com.example.finalproject.control;

import android.util.Log;

import com.example.finalproject.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FireStoreService {
    private FirebaseAuth mAuth;
    private static FireStoreService instance;
    private FirebaseFirestore database;
    private DatabaseReference databaseReference;
    private static String TAG = "FireStoreService";

    private CollectionReference usersRef;

    private FireStoreService (){
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        usersRef = database.collection("Users");
    }
    public static void initService(){
        instance= new FireStoreService();
    }
    public static FireStoreService getInstance(){
        if (instance == null)
            instance=new FireStoreService();

        return instance;
    }

    public static Task<DocumentReference> addUser(User user){
        return instance.database.collection("Users")
                .add(user.toHashMap());
    }

    public static Task<QuerySnapshot> authenticate(String email, String password){
        CollectionReference usersRef = instance.database.collection("Users");
        Query query = usersRef
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1);
        return query.get();
    }

    public static Task<DocumentSnapshot> findUser(String id){
        try{
            DocumentReference user = instance.usersRef.document(id);
            return user.get();
        } catch(Exception e){
            Log.d("<Exception>", e.getMessage());
        }
        return null;
    }
    public static Task<Void> addSubjectToTeacher(String teacherId, String subjectName) {
        DocumentReference teacherRef = instance.usersRef.document(teacherId);
        return teacherRef.update("subjects", FieldValue.arrayUnion(subjectName));
    }

}
