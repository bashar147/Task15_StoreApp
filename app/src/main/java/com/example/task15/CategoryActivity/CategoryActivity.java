package com.example.task15.CategoryActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.Activity.CartActivity;
import com.example.task15.Activity.CheckOut;
import com.example.task15.Adapter.SliderAdapter;
import com.example.task15.Classes.CartModel;
import com.example.task15.Classes.SliderItem;
import com.example.task15.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private String imageUrl;
    private String Name;
    private long cartPrice;
    private long COUNTER;
    private String madIn;
    private String desc;
    private CartModel cartModel;
    private long x;
    private SliderView sliderView;
    private ArrayList<SliderItem> sliderItems;

    private TextView name , made , price ,description , counter;
    private ImageButton plus , del ;
    private Button addToCart  , byNow;
    private ImageView  back , clickShop;
    SliderAdapter adapter;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initialize();
        getData();

        name.setText(Name);
        made.setText(madIn);
        price.setText(""+ cartPrice);
        counter.setText(""+COUNTER);
        description.setText(""+desc);
        cartModel = new CartModel();
        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(imageUrl));

       sliderView = findViewById(R.id.imageSlider);

       adapter = new SliderAdapter(this,sliderItems);
       getSliderItems();
       sliderView.setSliderAdapter(adapter);
       sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
       sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
       sliderView.startAutoCycle();
   }

   public void initialize(){

       addToCart = findViewById(R.id.categoryAddToCart);
       name = findViewById(R.id.categoryClickName);
       made = findViewById(R.id.catergoryClickMadeIn);
       price = findViewById(R.id.categoryClickPrice);
       description = findViewById(R.id.catergoryClickDescription);
       counter = findViewById(R.id.CategoryClickCounterText);
       plus = findViewById(R.id.plusImageBtn);
       plus.setOnClickListener(this::PlusClick);
       del = findViewById(R.id.delImageBtn);
       del.setOnClickListener(this::DelClick);
       addToCart.setOnClickListener(this::AddToCart);
       byNow = findViewById(R.id.toByNowBtn);
       byNow.setOnClickListener(this::ByNow);
       back = findViewById(R.id.categoryClickBack);
       back.setOnClickListener(this::BackClick);
       clickShop = findViewById(R.id.CategoryClickShop);
       clickShop.setOnClickListener(this::ClickShop);

       SharedPreferences prefs = getSharedPreferences("category counter", MODE_PRIVATE);
            x=prefs.getLong(Name,0);
   }

    private void DelClick(View view) {

       int count1 = Integer.parseInt(counter.getText().toString());
       if (count1>0) {
           count1--;
           counter.setText("" + count1);
       }else {

       }

    }

    private void PlusClick(View view) {
        int count1 = Integer.parseInt(counter.getText().toString());
        count1++;
        counter.setText(""+count1);
    }

    private void ClickShop(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    private void BackClick(View view) {
        finish();
    }

    private void ByNow(View view) {
        int count1 = Integer.parseInt(counter.getText().toString());
        if (count1>0){
            AddToCart(view);
            Intent intent1 = new Intent(getApplicationContext(), CheckOut.class);
            intent1.putExtra("_price_",cartPrice);
            startActivity(intent1);
        }else {
            Toast.makeText(getApplicationContext(),"Please add at least on item " , Toast.LENGTH_SHORT).show();
        }
    }

    private void AddToCart(View view) {
        int count1 = Integer.parseInt(counter.getText().toString());

        if (count1>0){
            DatabaseReference orderRef  = FirebaseDatabase.getInstance().getReference("orders")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                      getSnapData(orderRef,snapshot);
                    }else {
                        DatabaseReference databaseReference4  = FirebaseDatabase.getInstance().getReference("orders")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("boi");
                        databaseReference4.setValue("1");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"Please add at least on item " , Toast.LENGTH_SHORT).show();
        }


    }

    public void getData(){

       Intent intent = getIntent();
       Name = intent.getExtras().getString("name");
       Log.println(Log.ASSERT,"sae",""+Name);
       imageUrl = intent.getExtras().getString("url");
        cartPrice = intent.getExtras().getLong("price");
       COUNTER = intent.getExtras().getLong("counter");
       madIn = intent.getExtras().getString("madeIn");
       desc = intent.getExtras().getString("description");

    }

    public void getSliderItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    sliderItems.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        sliderItems.add(new SliderItem(dataSnapshot.child("imageUrl").getValue(String.class)));
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    if (sliderItems.size()<1){
                        sliderItems.clear();
                       sliderItems.add(new SliderItem(imageUrl));
                        adapter.notifyDataSetChanged();

                        //Log.println(Log.ASSERT,"Here","From slider size <1");
                    }else if (sliderItems.size()>=1){
                        //linearLayout.setVisibility(View.GONE);
                        //Log.println(Log.ASSERT,"Here","From slider size <1");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error...\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getSnapData(DatabaseReference  databaseReference,@NonNull DataSnapshot snapshot){

        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
            String textViewName = name.getText().toString();
            String dbName = dataSnapshot.child("name").getValue(String.class);

            if (textViewName.equals(dbName)){
                Toast.makeText(getApplicationContext(), "already exists in your cart", Toast.LENGTH_SHORT).show();
                break;
            }else {

                x=Long.parseLong(counter.getText().toString());
                SharedPreferences.Editor editor = getSharedPreferences("category counter", MODE_PRIVATE).edit();
                editor.putLong(Name, x);
                editor.apply();
                Log.println(Log.ASSERT,"xValue","x="+x);

                DatabaseReference databaseReference4  = FirebaseDatabase.getInstance().getReference("orders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("boi");
                databaseReference4.setValue(null);
                DatabaseReference databaseReference2  = FirebaseDatabase.getInstance().getReference("orders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Name);

                cartModel = new CartModel(imageUrl,Name,x, cartPrice);

                databaseReference2.setValue(cartModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Added To Card Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });

                break;
            }
        }
    }
}