package com.example.openarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String userType;
    private String userEmail;
    private String userPassword;
    private final String TAG = "Login: ";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        TextView email = findViewById(R.id.loginEmailInput);
        TextView password = findViewById(R.id.loginPasswordInput);
        Button logIn = findViewById(R.id.loginButton);
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        //Function to Signin and on click listener for logIn button
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG,"signInWithCustomEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    CollectionReference students = db.collection("students");
                                    CollectionReference counselors = db.collection("counselors");
                                    Query studentQuery = students.whereEqualTo("ID", user.getUid());
                                    Query counselorQuery = counselors.whereEqualTo("ID",user.getUid());
                                    if (studentQuery != null){
                                        userType="student";
                                        updateUI();
                                    }else if (counselorQuery !=null){
                                        userType="counselor";
                                        updateUI();
                                    }else{
                                        Toast.makeText(Login.this, "Could not find you as a student or counselor", Toast.LENGTH_LONG);
                                    }
                                }
                            }
                        });
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    public void updateUI(){
        if (userType.equals("student")){
            startActivity(new Intent(this, StudentHomePage.class));
        } else{
            startActivity(new Intent(this, CounselorHomePage.class));
        }

    }
}