package com.example.finalproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalproject.control.FireStoreService;
import com.example.finalproject.model.User;
import com.example.finalproject.model.UserRole;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        createEvents();
    }

    private void createEvents(){
        submitBtn.setOnClickListener(view -> {
            User user = createUser();
            if (user != null) {
                createAccount(user);
            }
        });
    }


    private void findViews() {
        emailInput= findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        submitBtn = findViewById(R.id.register_BTN_submit);
        nameInput= findViewById(R.id.register_fullName);
        ageInput = findViewById(R.id.register_age);
        phoneNumberInput= findViewById(R.id.register_phoneNumber);
        role = findViewById(R.id.register_role);
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
            if (user_role.equals("Teacher"))
                user_role = "TEACHER";
            if (user_role.equals("Student"))
                user_role = "STUDENT";
            UserRole role = UserRole.valueOf(user_role);
            user = new User(email, name, password, phone, age, user_role);
            Toast.makeText(this, "Register Succeed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding user", e);

                });

    }

}
