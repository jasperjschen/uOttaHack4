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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String userType;
    TextView email;
    TextView password;
    Button logIn;
    TextView signupStudent;
    TextView signupCounselor;
    private String userEmail;
    private String userPassword;
    private final String TAG = "Login: ";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        email = findViewById(R.id.loginEmailInput);
        password = findViewById(R.id.loginPasswordInput);
        logIn = findViewById(R.id.loginButton);
        signupCounselor = findViewById(R.id.LoginCounselorSignup);
        signupStudent = findViewById(R.id.LoginStudentSignup);


        signupStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, StudentSignup.class));
            }
        });
        signupCounselor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CounselorSignup.class));
            }
        });

        //Function to Signin and on click listener for logIn button
        logIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                mAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG,"signInWithCustomEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DocumentReference docRef = db.collection("students").document(user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    userType="student";
                                                    updateUI(user);
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                    userType="counselor";
                                                    updateUI(user);
                                                    Log.d(TAG,user.getUid());
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });
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
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){

        if(user != null){
            if(userType==null){}
            else if (userType.equals("student")){
                startActivity(new Intent(this, StudentMainPage.class));
            } else{
                startActivity(new Intent(this, CounselorMainPage.class));
            }
        }
        else{
            Toast.makeText(Login.this, "You are not logged in.", Toast.LENGTH_LONG);
        }

    }
}