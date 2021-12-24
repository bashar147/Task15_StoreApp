package com.example.task15.Fragments.BottomNavFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.Activity.HomePageActivity;
import com.example.task15.Adapter.HomePageTabAdapter;
import com.example.task15.Adapter.OrderAdapter;
import com.example.task15.Classes.CartModel;
import com.example.task15.Classes.NotificationDialog;
import com.example.task15.Classes.OrderModel;
import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BottomBookFragment extends Fragment {

    public BottomBookFragment() {

    }
    private LinearLayout linearLayout;
    private Button button;

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ArrayList<OrderModel> models;

    private ImageView back , notification;

    public long _price_ = 0;
    public int product =0 ;

    private TextView notificationCounterText;

    @Override
    public View     onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bottom_book, container, false);

        linearLayout = view.findViewById(R.id.orderCardLinear);
        linearLayout.setVisibility(View.GONE);

        button = view.findViewById(R.id.ordersOrderNowButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomePageActivity.class));
                getActivity().finish();
            }
        });

        addRecycler(view);

        notificationCounterText = view.findViewById(R.id.notificationCounterText);
        back = view.findViewById(R.id.ordersClickBack);
        notification = view.findViewById(R.id.ordersNnotificationBell);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HomePageTabAdapter.class));
                getActivity().finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDialog notificationDialog = new NotificationDialog(getActivity());
                notificationDialog.show();
            }
        });

        return view;
    }

    public void addRecycler(@NonNull View view){
        models = new ArrayList<>();
        recyclerView = view.findViewById(R.id.orderRecycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new OrderAdapter(getContext(),models);
        addItems();
        recyclerView.setAdapter(adapter);

    }


    public void addItems(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    models.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Log.println(Log.ASSERT,"cartCounter",String.valueOf(dataSnapshot.child("cartCounter").getValue(long.class)));
                        Log.println(Log.ASSERT,"cartPrice",String.valueOf(dataSnapshot.child("cart_PRICE").getValue(long.class)));
                        product++;
                        _price_ += dataSnapshot.child("cart_PRICE").getValue(long.class);
                    }
                    models.add(new OrderModel(String.valueOf(product),
                            String.valueOf(_price_),"Completed"));

                    adapter.notifyDataSetChanged();

                }
                else {
                    if (models.size()<1){
                        linearLayout.setVisibility(View.VISIBLE);
                    }else if (models.size()>=1){
                        linearLayout.setVisibility(View.GONE);
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