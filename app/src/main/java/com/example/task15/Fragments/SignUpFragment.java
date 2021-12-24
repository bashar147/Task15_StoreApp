package com.example.task15.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task15.Activity.HomePageActivity;
import com.example.task15.Classes.SignUpModel;
import com.example.task15.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +  // at least 1 special character
                    "(?=\\S+$)" +                // no white spaces
                    ".{4,}" +                    // at least 4 characters
                    "$");
    boolean check = false;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public SignUpFragment() {

    }

    private EditText name, email, pass, repass;
    private Button signUp;
    private long order = 0;
    private SignUpModel model;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;

    private DatabaseReference reference;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        name = view.findViewById(R.id.signUp_fullName);
        email = view.findViewById(R.id.signUp_Email);
        pass = view.findViewById(R.id.signUp_passowrd);
        repass = view.findViewById(R.id.signUp_re_password);
        signUp = view.findViewById(R.id.signUp_Button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        signUp.setOnClickListener(this::SignUpClick);
        return view;
    }

    private void SignUpClick(View view) {
        boolean t1, t2;
        t1 = validatePassword();
        t2 = validateEmail();
        confirmInput(view);
        if (!pass.getText().toString().equals(repass.getText().toString())) {
            Toast.makeText(getContext(), "Password did not match ", Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().trim().equals("") || repass.getText().toString().trim().equals("")) {
            name.setError("Field Can't Be Empty");
            repass.setError("Field Can't Be Empty");

        } else if (t2 == true && t1 == true) {
            name.setError(null);
            repass.setError(null);
            registerUser(email.getText().toString().trim(), pass.getText().toString().trim());
        }
    }

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address \n" +
                    "Must have @ and .com ");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = pass.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass.setError("Field can not be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass.setError("Password is too weak" +
                    "\n" + "" + "at least 4 characters and no spaces " +
                    "\n" + "at least 1 special character");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        check = true;
    }

    private void registerUser(String text_email, String text_password) {
        auth.createUserWithEmailAndPassword(text_email, text_password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String Name = name.getText().toString().trim();
                            String Email = email.getText().toString().trim();
                            String Pass = pass.getText().toString().trim();
                            String RePass = repass.getText().toString().trim();

                            model = new SignUpModel(Name, Email, Pass, RePass, order,"https://firebasestorage.googleapis.com/v0/b/task15-2a04b.appspot.com/o/profile%20pic%2F1640159224690.jpg?alt=media&token=f533ed99-73ae-4e07-a501-3f8bb55d1f26");

                            reference = firebaseDatabase.getReference("users")
                                    .child(auth.getCurrentUser().getUid());

                            reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("users")
                                                    .child(auth.getCurrentUser().getUid())
                                                    .child("phoneNumber");

                                            DatabaseReference databaseReference2 = firebaseDatabase.getReference("users")
                                                    .child(auth.getCurrentUser().getUid())
                                                    .child("complete");

                                        DatabaseReference databaseReference = firebaseDatabase.getReference("users")
                                                .child(auth.getCurrentUser().getUid())
                                                .child("location");

                                            databaseReference2.setValue("0");
                                            databaseReference1.setValue("no phone added");
                                            databaseReference.setValue("No Location Found");

                                            Toast.makeText(getContext(), "Registering user successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), HomePageActivity.class));
                                            getActivity().finish();

                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Cannot Upload User Data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Registration Failed! \n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

