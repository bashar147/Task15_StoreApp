package com.example.task15.CategoryActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.Activity.CartActivity;
import com.example.task15.Adapter.CategoryAdapter;
import com.example.task15.Classes.CategoryModel;
import com.example.task15.Classes.Model;
import com.example.task15.Fragments.BottomNavFragments.BottomHomeFragment;
import com.example.task15.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WomenActivity extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private RecyclerView recyclerView ;
    private CategoryAdapter adapter ;
    private ArrayList<CategoryModel> categoryModels ;

    private TextView Title ;
    private ImageView back , shop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women);

        Title =  findViewById(R.id.categoryTitle);
        firebaseDatabase = FirebaseDatabase.getInstance();
        back = findViewById(R.id.categoryBack);
        shop = findViewById(R.id.CategoryBell);

        shop.setOnClickListener(this::ShopClick);
        back.setOnClickListener(this::BackClick);
        createRecycler();
    }

    private void ShopClick(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    private void BackClick(View view) {
        finish();
    }

    public void createRecycler(){
        addItem();
        recyclerView = findViewById(R.id.categoryRecycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        adapter = new CategoryAdapter(this,categoryModels);

        recyclerView.setAdapter(adapter);
    }

    public void addItem(){
        categoryModels = new ArrayList<>();
        if(BottomHomeFragment.TAP_INDEX == 0 ){
            getValues("items","woman");
            Title.setText("Woman");
        }else if (BottomHomeFragment.TAP_INDEX == 1){
            getValues("items","man");
            Title.setText("Man");
        }else if (BottomHomeFragment.TAP_INDEX == 2){
            getValues("items","kid");
            Title.setText("Kid");
        }else if (BottomHomeFragment.TAP_INDEX == 3){
            getValues("items","all");
            Title.setText("All Category");
        }

    }


    public void getValues(String Reference, String child){
        reference = firebaseDatabase.getReference(Reference)
                .child(child);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    categoryModels.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        categoryModels.add(new CategoryModel(
                         dataSnapshot.child("url").getValue(String.class)
                        ,dataSnapshot.child("name ").getValue(String.class)
                        ,dataSnapshot.child("price").getValue(long.class)
                        ,dataSnapshot.child("counter").getValue(long.class)
                        , dataSnapshot.child("madeIn").getValue(String.class)
                        , dataSnapshot.child("description").getValue(String.class)
                        ));

                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}