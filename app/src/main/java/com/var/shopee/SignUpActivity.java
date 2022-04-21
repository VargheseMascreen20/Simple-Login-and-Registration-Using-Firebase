package com.var.shopee;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    EditText emailTxt, passwordTxt;
    Button signUpBtn;
    String userID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString().trim();
                final String password = passwordTxt.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailTxt.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordTxt.setError("Password is Required");
                    return;
                }
                if (password.length() < 6) {
                    passwordTxt.setError("Password Length must be >= 6 Characters");
                    return;
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = mAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "Verificatiion Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                Toast.makeText(SignUpActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid();
                                Toast.makeText(SignUpActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });


                }
            }


        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

}