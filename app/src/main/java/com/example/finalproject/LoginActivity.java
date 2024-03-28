package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.example.finalproject.model.User;
import com.example.finalproject.model.UserRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;

//    private Map<String, Intent> startNext = Map.of(
//            "TEACHER", new Intent(LoginActivity.this, TeacherActivity.class)
//    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.login_email);
        mPasswordField = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_BTN_submit);

        mLoginButton.setOnClickListener(v -> signIn(mEmailField.getText().toString(), mPasswordField.getText().toString()));

        mEmailField.setText("yarden@gmail.com");
        mPasswordField.setText("yarden1234");
    }

    private void signIn(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        FireStoreService.authenticate(email, password).addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                LocalStorageService.updateUser(docId);
//                startActivity(startNext.get(LocalStorageService.getUser().get("role")));
                if(LocalStorageService.getUser().get("role").toString().equals(UserRole.TEACHER.toString())){
                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                }
            } else {
                Log.w(TAG, "signInWithEmail:failure");
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    private void checkUserRole(FirebaseUser user) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User userData = snapshot.getValue(User.class);
                    if (userData != null) {
                        String role = String.valueOf(userData.getRole());
                        if (role.equals("teacher")) {
                            // User is a teacher, redirect to teacher activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Prevent going back to login activity
                        } else if (role.equals("student")) {
                            // User is a student, redirect to student activity
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish(); // Prevent going back to login activity
                        } else {
                            Toast.makeText(LoginActivity.this, "Unknown user role", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Failed to check user role", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


