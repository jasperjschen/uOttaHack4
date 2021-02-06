package com.example.openarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CounselorSignup extends AppCompatActivity {
    Map<String, Object> userInfo = new HashMap<>();
    String userName;
    String userPass;
    String userEmail;
    String userLanguages;
    String userSpecialty;
    TextView email;
    TextView pass;
    TextView name;
    Button signUp;
    TextView login;
    TextView languages;
    boolean[] selectedLanguage;
    ArrayList<Integer> languageList = new ArrayList<>();
    String[] languageArray = {"English", "French", "Spanish"};
    TextView specialty;
    boolean[] selectedSpecialty;
    String[] specialtyArray = {"Specialty1", "Specialty2", "Specialty3", "Specialty4"};
    ArrayList<Integer> specialtyList = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "CounselorSignup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_signup);
        email = findViewById(R.id.counselorSignupEmailInput);
        pass = findViewById(R.id.counselorSignupPasswordInput);
        name = findViewById(R.id.counselorSignupNameInput);
        signUp = findViewById(R.id.counselorSignupButton);
        login = findViewById(R.id.counselorSignupHaveAccountTextView);
        languages = findViewById(R.id.counselorSignupLanguageSelect);
        specialty = findViewById(R.id.counselorSignupSpecializationSelect);

        mAuth = FirebaseAuth.getInstance();

        selectedLanguage = new boolean[languageArray.length];
        selectedSpecialty = new boolean[specialtyArray.length];

        //Language and Specialization drop down
        languages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        CounselorSignup.this
                );
                //Set title
                builder.setTitle("Please Select a Language");
                //Set dialog non cancelable
                builder.setCancelable(true);
                builder.setMultiChoiceItems(languageArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        //Check condition
                        if (isChecked){
                            //When checkbox selected add position in language list
                            languageList.add(which);
                            //Sort language list
                            Collections.sort(languageList);
                        }else {
                            //When checkbox unselected remove position from language list
                            languageList.remove(which);
                        }
                    }
                });
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Initialize String builder
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i<languageList.size();i++){
                            //Concat array value
                            stringBuilder.append(languageArray[languageList.get(i)]);
                            //Check condition
                            if (i != languageList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        //Set text on text view
                        languages.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0; i<selectedLanguage.length;i++ ){
                            selectedLanguage[i]= false;
                            languageList.clear();
                            languages.setText("");
                        }
                    }
                });
                //Show dialog
                builder.show();
            }
        });
        specialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        CounselorSignup.this
                );
                //Set title
                builder.setTitle("Please Your Specializations");
                //Set dialog non cancelable
                builder.setCancelable(true);
                builder.setMultiChoiceItems(specialtyArray, selectedSpecialty, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        //Check condition
                        if (isChecked){
                            //When checkbox selected add position in language list
                            specialtyList.add(which);
                            //Sort language list
                            Collections.sort(specialtyList);
                        }else {
                            //When checkbox unselected remove position from language list
                            specialtyList.remove(which);
                        }
                    }
                });
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Initialize String builder
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i< specialtyList.size();i++){
                            //Concat array value
                            stringBuilder.append(specialtyArray[specialtyList.get(i)]);
                            //Check condition
                            if (i != specialtyList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        //Set text on text view
                        specialty.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i=0; i<selectedSpecialty.length;i++ ){
                            selectedSpecialty[i]= false;
                            specialtyList.clear();
                            specialty.setText("");
                        }
                    }
                });
                //Show dialog
                builder.show();
            }
        });

        //Move to Login page
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CounselorSignup.this, Login.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = name.getText().toString();
                userPass = pass.getText().toString();
                userEmail = email.getText().toString();
                userLanguages = languages.getText().toString();
                userSpecialty = specialty.getText().toString();
                if (TextUtils.isEmpty(languages.getText())){
                    languages.setError("Please select a language");
                }
                else if (TextUtils.isEmpty(specialty.getText())){
                    specialty.setError("Please select your specialization");
                }
                else{
                    mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Log.d(TAG,"createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName).build();
                                        user.updateProfile(profileUpdates);
                                        userInfo.put("Name", user.getDisplayName());
                                        userInfo.put("Email", user.getEmail());
                                        userInfo.put("ID", user.getUid());
                                        userInfo.put("Specialization", userSpecialty);
                                        userInfo.put("Languages", userLanguages);
                                        db.collection("counselors").document(user.getUid())
                                                .set(userInfo);
                                        updateUI(user);
                                    }
                                }
                            });
                }
            }
        });


    }
    public  void  updateUI(FirebaseUser user){
        if (user != null){
            Toast.makeText(this, "You Signed In Successfully!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, CounselorHomePage.class));
        }else{
            Toast.makeText(this, "Something went wrong please check your entries.",Toast.LENGTH_LONG).show();
        }
    }
}