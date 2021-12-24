package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.Adapter.SearchAdapter;
import com.example.task15.CategoryActivity.WomenActivity;
import com.example.task15.Classes.CartModel;
import com.example.task15.Classes.CategoryModel;
import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private LinearLayout searchLinear;
    private RecyclerView recyclerView;

    private SearchAdapter adapter;
    private ArrayList<CategoryModel>  searchModels;

    private TextView foundText;
    private EditText search;

    private ImageView searchClickBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchModels = new ArrayList<>();
        addItems();
        foundText = findViewById(R.id.searchRresultText);
        search = findViewById(R.id.searchSEarchEditTExt);
        searchLinear = findViewById(R.id.searchLinear);
        recyclerView = findViewById(R.id.searchRecycler);
        //recyclerView.setVisibility(View.GONE);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        adapter = new SearchAdapter(this,searchModels);

        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                foundText.setText("Found "+SearchAdapter.size+" results");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recyclerView.setVisibility(View.VISIBLE);
                ((SearchAdapter) adapter).search(s);
                foundText.setText("Found "+SearchAdapter.size+" results");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchClickBack = findViewById(R.id.searchClickBack);
        searchClickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addItems(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items")
                .child("all");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    searchModels.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        searchModels.add(new CategoryModel(
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
                else {
                    if (searchModels.size()<1){
                        searchLinear.setVisibility(View.VISIBLE);
                    }else if (searchModels.size()>=1){
                        searchLinear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}