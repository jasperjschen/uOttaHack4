package com.example.openarms;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentProfileFragment extends Fragment {
    View view;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    String userEmail;
    String userPass;

    private final String TAG = "StudentProfileFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_student_fragment_profile, container, false);
        TextView email = view.findViewById(R.id.studentProfileEmailInput);
        TextView pass = view.findViewById(R.id.studentProfilePasswordInput);
        Button logout = view.findViewById(R.id.studentProfileLogOutButton);
        ImageView profilePic = view.findViewById(R.id.studentProfileImageView);
        TextView name = view.findViewById(R.id.studentProfileNameTextView);
        Button change = view.findViewById(R.id.studentProfileSubmitChangeButton);
        FirebaseUser user = mAuth.getCurrentUser();
        email.setText(user.getEmail());
        name.setText(user.getDisplayName());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPass = pass.getText().toString();
                user.updateEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
                user.updatePassword(userPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password address updated.");
                        }
                    }
                });
                Toast.makeText(getActivity(), "Your email and password have been changed",Toast.LENGTH_LONG);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(),Login.class));
            }
        });

        return view;
    }
}

