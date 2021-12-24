package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.task15.Adapter.CartAdapter;
import com.example.task15.Classes.CartModel;
import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private CartAdapter adapter;
    private ArrayList<CartModel> cartModels;
    private LinearLayout linearLayout;
    private Button cartCheckOutButton;
    private ImageView back;

    public long _price_ = 0;
    public int product =0 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.activity_cart);

        linearLayout = findViewById(R.id.cardLinear);
        cartModels = new ArrayList<>();

        recyclerView = findViewById(R.id.cartRecycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CartAdapter(this , cartModels);

        addItems();
        recyclerView.setAdapter(adapter);

        cartCheckOutButton = findViewById(R.id.cartCheckOutButton);
        cartCheckOutButton.setOnClickListener(this::cartButtonClick);

        back = findViewById(R.id.cartClickBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                finish();
            }
        });
    }

    private void cartButtonClick(View view) {
        Intent intent =  new Intent(getApplicationContext(),CheckOutActivity.class);
        intent.putExtra("_price_",_price_);
        startActivity(intent);
        finish();
    }

    public void addItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        cartModels.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Log.println(Log.ASSERT,"cartCounter",String.valueOf(dataSnapshot.child("cartCounter").getValue(long.class)));
                            Log.println(Log.ASSERT,"cartPrice",String.valueOf(dataSnapshot.child("cart_PRICE").getValue(long.class)));
                            product++;
                            _price_ += dataSnapshot.child("cart_PRICE").getValue(long.class);
                            cartModels.add(new CartModel(
                                     dataSnapshot.child("imageUrl").getValue(String.class)
                                    ,dataSnapshot.child("name").getValue(String.class)
                                    ,dataSnapshot.child("cartCounter").getValue(long.class)
                                    ,dataSnapshot.child("cart_PRICE").getValue(long.class)
                            ));
                        }
                        adapter.notifyDataSetChanged();

                    }
                    else {
                        if (cartModels.size()<1){
                            linearLayout.setVisibility(View.VISIBLE);
                        }else if (cartModels.size()>=1){
                            linearLayout.setVisibility(View.GONE);
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