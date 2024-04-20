package com.example.finalproject.control;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.finalproject.model.Lesson;
import com.example.finalproject.model.User;
import com.example.finalproject.model.UserRole;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.Date;


public class FireStoreService {

    private static FireStoreService instance;
    private FirebaseFirestore database;
    private CollectionReference usersRef;
    private CollectionReference lessonsRef;

    private FireStoreService (){
        database = FirebaseFirestore.getInstance();
        usersRef = database.collection("Users");
        lessonsRef = database.collection("Lessons");
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
        return instance.usersRef.add(user.toHashMap());
    }

    public static void addLesson(Lesson lesson){
        instance.lessonsRef.add(lesson.toHashMap()).onSuccessTask(new SuccessContinuation<DocumentReference, Object>() {

            @NonNull
            @Override
            public Task<Object> then(DocumentReference documentReference) throws Exception {
                documentReference
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                addLessonToUser(lesson.getStudentId(), value.getId());
                                addLessonToUser(lesson.getTeacherId(), value.getId());

                            }
                        });


                return null;
            }
        });

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

    public static Task<Void> addLessonToUser(String userId, String docId){
        DocumentReference userRef = instance.usersRef.document(userId);
        return userRef.update("Lessons",FieldValue.arrayUnion(docId));
    }

    public static Task<QuerySnapshot> getListOfSubjects(){
        return instance.usersRef.whereEqualTo("role", UserRole.TEACHER.toString())
                .get();
    }

    public static Task<QuerySnapshot> getStudentLessons(String studentId){
        return instance.lessonsRef.whereEqualTo("student", studentId)
                .get();
    }

    public static Task<QuerySnapshot> getTeacherLessons(String teacherID){
        return instance.lessonsRef.whereEqualTo("teacher", teacherID)
                .get();
    }


    public static Task<Void> updateDays(String teacherId, String day, String hours){
        DocumentReference teacherRef = instance.usersRef.document(teacherId);
        return teacherRef.update(day, hours);
    }

    public static Task<Void> updateAboutTeacher(String teacherId,String aboutTeacher){
        DocumentReference teacherRef = instance.usersRef.document(teacherId);
        return  teacherRef.update("About me",aboutTeacher);
    }

    public static Task<QuerySnapshot> filterLessons(String date, String teacherId, String studentId){

        Query query = instance.lessonsRef.whereEqualTo("date", date);
        return query.get();
    }

    public static Task<Void> updateLessonCost(String teacherId,String cost){
        DocumentReference teacherRef = instance.usersRef.document(teacherId);
        return  teacherRef.update("Cost",cost);
    }

    public static Task<Void> deleteLesson(String lessonId, String teacher, String student){
        WriteBatch batch = instance.database.batch();

        // Delete the lesson document from the Lessons collection
        DocumentReference lessonDocRef = instance.lessonsRef.document(lessonId);
        batch.delete(lessonDocRef);

        // Remove the lesson ID from the lessons array in the respective user documents
        DocumentReference teacherDocRef = instance.usersRef.document(teacher);
        DocumentReference studentDocRef = instance.usersRef.document(student);

        // Remove the lesson ID from the teacher's lessons array
        batch.update(teacherDocRef, "Lessons", FieldValue.arrayRemove(lessonId));

        // Remove the lesson ID from the student's lessons array
        batch.update(studentDocRef, "Lessons", FieldValue.arrayRemove(lessonId));

        // Commit the batch operation
        return batch.commit();

    }

}
