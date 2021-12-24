package com.example.task15.Fragments.BottomNavFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task15.Activity.CartActivity;
import com.example.task15.Activity.SearchActivity;
import com.example.task15.Adapter.HomePageRecyclerAdapter;
import com.example.task15.Adapter.HomePageTabAdapter;
import com.example.task15.Classes.Model;
import com.example.task15.Classes.ViewPagerAdapter;
import com.example.task15.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BottomHomeFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    public BottomHomeFragment() {
        // Required empty public constructor
    }

    private Button search;
    private RecyclerView recyclerView;
    private HomePageRecyclerAdapter adapter;
    private ArrayList<Model> models;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HomePageTabAdapter homePageTabAdapter;
    private ImageView imageView;

    public static int TAP_INDEX=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_home, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        models = new ArrayList<>();
        imageView = view.findViewById(R.id.bottomFragShopIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        });


        recyclerView  = view.findViewById(R.id.homePageRecyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        search= view.findViewById(R.id.homeSearchView);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        reference = firebaseDatabase.getReference("items")
        .child("woman");


        adapter = new HomePageRecyclerAdapter(getContext(),models);

        recyclerView.setAdapter(adapter);

        viewPager = view.findViewById(R.id.homePage_pager);
        tabLayout = view.findViewById(R.id.homePage_tab_layout);

        homePageTabAdapter = new HomePageTabAdapter(
                getActivity().getSupportFragmentManager()
                ,"Woman","Man","Kids","All");

        viewPager.setAdapter(homePageTabAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                TAP_INDEX = position;

                if (position == 0 ){
                    getValues("items","woman");
                } else if (position == 1){
                    getValues("items","man");
                } else if (position==2){
                    getValues("items","kid");
                } else if (position==3){
                    getValues("items","all");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        models.add(new Model(dataSnapshot.child("url").getValue(String.class)));
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "Snap shopnt not exisist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    public void getValues(String Reference, String child){
       reference = firebaseDatabase.getReference(Reference)
               .child(child);

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               if (snapshot.exists()){
                   models.clear();
                   for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                       models.add(new Model(dataSnapshot.child("url").getValue(String.class)));
                   }
                   adapter.notifyDataSetChanged();
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(getActivity(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

}