package com.example.task15.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class ForgetPasswordFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Random random = new Random();
    public ForgetPasswordFragment() {

    }

    private Button buttonSend ,check , change;
    private EditText email , pass ;
    private TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        initialize(view);

        return view;
    }

    public void initialize(@NonNull View view){

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = firebaseDatabase.getReference("users");

        buttonSend = view.findViewById(R.id.forgetSendCodeButton);
        change = view.findViewById(R.id.forgetChangeButton);
        check = view.findViewById(R.id.forgetCheckButton);

        email = view.findViewById(R.id.forget_EmailAndCode);
        pass = view.findViewById(R.id.forget_Password);

        textView = view.findViewById(R.id.forgetText);

        buttonSend.setOnClickListener(this::buttonSendClick);
        check.setOnClickListener(this::buttonCheckClick);
        change.setOnClickListener(this::buttonChangeClick);

    }

    public String KEY ="";
    public void buttonSendClick(View view){

        String email_text = email.getText().toString().trim();
        if (email_text.equals("")){
            Toast.makeText(getContext(), "Please Enter The Email", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email_text).matches()){
                email.setError("Please Provide Valid Email");
                email.requestFocus();
                return;
        } else {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            String emailDB = snapshot1.child("email").getValue(String.class);
                            if (emailDB.equals(email_text)){
                                KEY = snapshot1.getKey();
                                Log.println(Log.ASSERT,"key","KEY= "+KEY);
                                //noreply@task15-2a04b.firebaseapp.com
                                String to = email_text;
                                String from = "noreply@task15-2a04b.firebaseapp.com";
                                String host = "infoa.com";

                                //sendEmail(email_text,"123456789");

                                textView.setText("Enter verification code.");
                                email.setText("");
                                email.setHint("Code");
                                buttonSend.setVisibility(View.GONE);
                                check.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    public void buttonChangeClick(View view){

    }
    public void buttonCheckClick(View view){
        textView.setText("Enter your new password.");
        email.setHint("Password");
        pass.setVisibility(View.VISIBLE);
        check.setVisibility(View.GONE);
        change.setVisibility(View.VISIBLE);
    }

    public void sendEmail(String send_email , String password){
        String messageToSend = "RestBass";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties
                , new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(send_email,password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(send_email));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(send_email));
            message.setSubject("Reset Password");
            int x = random.nextInt(1000);
            message.setText(""+x);
            Transport.send(message);
            Toast.makeText(getActivity(), "Check your email to reset password", Toast.LENGTH_SHORT).show();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}