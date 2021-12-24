package com.example.task15.Classes;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationDialog  extends Dialog {

    public Activity c;
    public Dialog d;
    private TextView textView;
    public NotificationDialog(Activity a) {
        super(a);
        this.c = a;

    }
        private TextView Clear;
        private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notification_dialog);

        back = findViewById(R.id.dialogCancel);
        Clear = findViewById(R.id.clear_text);
        Clear.setOnClickListener(this::ClearClick);
        textView = findViewById(R.id.noteText);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getData();
    }

    private void ClearClick(View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notification")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    databaseReference.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notification")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("note1");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                        textView.setText(""+snapshot.getValue(String.class));
                }else {
                    textView.setText("No \nNotifications\nYet!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}


