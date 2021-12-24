package com.example.task15.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.Activity.HomePageActivity;
import com.example.task15.Classes.ViewPagerAdapter2;
import com.example.task15.MainActivity;
import com.example.task15.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    ViewPagerAdapter2 viewPagerAdapter2;

    public LoginFragment() {

    }

    private TextView forgetTExt;
    private EditText pass , email;
    private Button Login;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false) ;

        auth =FirebaseAuth.getInstance();
        pass = view.findViewById(R.id.loginPass);
        email = view.findViewById(R.id.loginEmail);


//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(getActivity(), HomePageActivity.class));
//            getActivity().finish();
//        }

        forgetTExt = view.findViewById(R.id.forgetPass);
        forgetTExt.setOnClickListener(this::forgetClick);

        Login = view.findViewById(R.id.loginButton);
        Login.setOnClickListener(this::login);
        return view;

    }

    private void login(View view) {
        LoginClick(view);
        //startActivity(new Intent(getActivity(),HomePageActivity.class));
    }

    private void forgetClick(View view) {

        viewPagerAdapter2 = new ViewPagerAdapter2(getActivity().getSupportFragmentManager(),"ForgetBass",getContext());
        MainActivity.tabLayout.setVisibility(View.GONE);
        MainActivity.viewPager.setVisibility(View.GONE);
        MainActivity.forgetTab.setVisibility(View.VISIBLE);
        MainActivity.forgetPAger.setVisibility(View.VISIBLE);
        MainActivity.forgetPAger.setAdapter(viewPagerAdapter2);
        MainActivity.forgetTab.setupWithViewPager(MainActivity.forgetPAger);
    }


    public void LoginClick(View view){
        String text_email= email.getText().toString().trim();
        String text_password = pass.getText().toString().trim();
        if (text_email.isEmpty() || text_password.isEmpty()){
            Toast.makeText(getContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }else {
            loginUsers(view,text_email,text_password);
        }
    }

    private void loginUsers(View view,String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Log.println(Log.ASSERT,"login",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), HomePageActivity.class));
                                getActivity().finish();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    DatabaseReference usersRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


}
