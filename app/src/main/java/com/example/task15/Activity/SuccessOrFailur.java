package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class SuccessOrFailur extends AppCompatActivity {

    private ImageView  imageView;
    private TextView text1 , idText;
    public  static String getComplete ="1";
    private Button home;
    private ImageView successOrFBack;
    private Random random;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succsess_or_fauiler);
        random = new Random();

        long x = random.nextInt(20000);
        imageView = findViewById(R.id.successorfImage);
        text1 = findViewById(R.id.successofFtext1);
        successOrFBack = findViewById(R.id.successOrFBack);

        DatabaseReference reference123 =  FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("id");

        reference123.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    reference123.setValue(x);
                }else {
                    reference123.setValue(x);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        idText =findViewById(R.id.successOrFIdText);
        home = findViewById(R.id.successorFButton);
        home.setOnClickListener(this::buttonClick);

        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("complete");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    getComplete = snapshot.getValue(String.class);
                    Log.println(Log.ASSERT,"getComplete",getComplete);
                }else {
                    reference.setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (getComplete.equals("1")) {
            Log.println(Log.ASSERT,"getComplete==1",getComplete);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("id");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        imageView.setBackgroundResource(R.drawable.success);
                        text1.setText("Order Completed");
                        idText.setText("Your Order ID:"+ snapshot.getValue(long.class)+" al\n" +
                                "Thank You!");
                    }else {
                       //databaseReference.setValue(x);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            imageView.setBackgroundResource(R.drawable.cross);
            text1.setText("Order Failed");
            idText.setText("Please check your information and\n" + "try again.");
        }
        successOrFBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buttonClick(View view) {
        startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
        finish();
    }

}