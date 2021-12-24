package com.example.task15.Fragments.BottomNavFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BottomNoteficationFragment extends Fragment {


    private TextView  textView;

    public BottomNoteficationFragment() {

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_notefication, container, false);
        textView = view.findViewById(R.id.noteFragmentTExt);
        getData();
        return  view;
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