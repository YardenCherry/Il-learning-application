package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.model.User;
import com.example.finalproject.model.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

public class RegisterActivity extends AppCompatActivity {

    private TextView emailInput;
    private TextView passwordInput;
    private TextView nameInput;
    private TextView ageInput;
    private TextView phoneNumberInput;
    private RadioGroup role;
    private Button submitBtn;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        setContentView(R.layout.activity_register);
        findViews();
        createEvents();
    }

    private void createEvents(){
        submitBtn.setOnClickListener(view -> {
            User user = createUser();
            if (user != null) {
                createAccount(user);
                //saveUserToFirebase(user);
            }
        });
    }


    private void findViews() {
        emailInput= findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        emailInput.setText("example@gmail.com");
        passwordInput.setText("examplePassword123");
        submitBtn = findViewById(R.id.register_BTN_submit);
        nameInput= findViewById(R.id.register_fullName);
        ageInput = findViewById(R.id.register_age);
        phoneNumberInput= findViewById(R.id.register_phoneNumber);
        role = findViewById(R.id.register_role);
        nameInput.setText("yardenc");
        ageInput.setText("20");
        phoneNumberInput.setText("0505500550");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private User createUser(){
        User user = null;
        try {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String name = nameInput.getText().toString();
            String age = ageInput.getText().toString();
            String phone = phoneNumberInput.getText().toString();
            Button selectedOption = findViewById(role.getCheckedRadioButtonId());
            String user_role = selectedOption.getText().toString();
            if (user_role.equals("מורה"))
                user_role = "TEACHER";
            if (user_role.equals("תלמיד/ה"))
                user_role = "STUDENT";
            UserRole role = UserRole.valueOf(user_role);
            user = new User(email, name, password, phone, age, user_role);
            Toast.makeText(this, "Register Succeed", Toast.LENGTH_SHORT).show();
        } catch (Exception ex){
            String message = ex.getMessage() != null ? ex.getMessage() : "AN UNEXPECTED ERROR OCCURRED";
           Snackbar.make(role, message, Snackbar.LENGTH_LONG).show();
        }
        return user;
    }

    private void createAccount(User user) {
        FireStoreService.addUser(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User added with ID: " + documentReference.getId());
                        if(user.getRole() == UserRole.STUDENT){
                            //go to student actiivty
                            //Intent intent = new Intent(MenuActivity.this, Student)
                        }
                        else{
                            // go to teacher activity
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding user", e);

                });
        updateUI(null);

    }
        private void reload() { }

        private void updateUI(FirebaseUser user) { }
//    private void saveUserToFirebase(User user) {
//        mDatabase.child(user.getUid()).setValue(user)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d(TAG, "User data saved to Firebase.");
//                    } else {
//                        Log.w(TAG, "Failed to save user data to Firebase.", task.getException());
//                    }
//                });
//    }
}
