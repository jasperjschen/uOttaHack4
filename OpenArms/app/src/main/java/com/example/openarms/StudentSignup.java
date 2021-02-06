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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentSignup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Map<String, Object> userInfo = new HashMap<>();
    String userEmail;
    String userPass;
    String userName;
    private final String TAG= "StudentSignup";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);
        mAuth = FirebaseAuth.getInstance();
        TextView email = findViewById(R.id.studentSignupEmailTextView);
        TextView pass = findViewById(R.id.studentSignupPasswordTextView);
        TextView name = findViewById(R.id.studentSignUpNameTextView);
        Button signUp = findViewById(R.id.studentSignUpButton);
        userEmail=email.getText().toString();
        userName = name.getText().toString();
        userPass = pass.getText().toString();

        // On Click listener for sign up button and the sign up function for firebase
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName).build();
                                    user.updateProfile(profileUpdates);
                                    userInfo.put("Name", user.getDisplayName());
                                    userInfo.put("Email", user.getEmail());
                                    userInfo.put("ID", user.getUid());
                                    db.collection("students").document(user.getUid())
                                            .set(userInfo);
                                    updateUI(user);
                                }
                            }
                        });
            }
        });
        //function to signup
            }
    public void updateUI(FirebaseUser user){
        if (user != null) {
            Toast.makeText(this, "You Signed In successfully!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, StudentHomePage.class));
        }else{
            Toast.makeText(this, "Something went wrong please check your email and password", Toast.LENGTH_SHORT).show();
        }
    }
}