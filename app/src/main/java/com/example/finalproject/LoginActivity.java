package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.control.LocalStorageService;
import com.example.finalproject.model.UserRole;
import com.google.firebase.firestore.DocumentSnapshot;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.login_email);
        mPasswordField = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_BTN_submit);
        mLoginButton.setOnClickListener(v -> signIn(mEmailField.getText().toString(), mPasswordField.getText().toString()));

    }

    private void signIn(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        FireStoreService.authenticate(email, password).addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot userDoc = queryDocumentSnapshots.getDocuments().get(0);
                LocalStorageService.updateUser(userDoc);
                if(LocalStorageService.getUser().get("role").toString().equals(UserRole.TEACHER.toString())){
                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));
                }
                else if(LocalStorageService.getUser().get("role").toString().equals(UserRole.STUDENT.toString())){
                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));
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

}


