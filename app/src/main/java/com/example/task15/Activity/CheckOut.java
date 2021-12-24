package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckOut extends AppCompatActivity {

    private RadioButton cash , card ;
    private RadioButton doorDelivery , pickUp;
    private EditText door ;
    private Button proceed_to_payment;
    private TextView checkoutPriceTotal;
    private ImageView checkOutClickBack;

    public static int SUCCESS_Or_Failure=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out2);

        initialize();

    }

    public void initialize(){

        checkoutPriceTotal = findViewById(R.id.checkoutPriceTotal);
        door = findViewById(R.id.cehckout2_doorEditText);
        card = findViewById(R.id.cardRadio);
        cash = findViewById(R.id.cashRadio);
        doorDelivery = findViewById(R.id.checkout2_doorRadio);
        pickUp = findViewById(R.id.checkout2pickup);
        proceed_to_payment = findViewById(R.id.proceed_to_payment);
        proceed_to_payment.setOnClickListener(this::ButtonClick);
        checkOutClickBack = findViewById(R.id.checkOutClickBack);
        checkOutClickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getIntentData();
    }

    private void ButtonClick(View view) {

        if (cash.isChecked() || cash.isSelected()){

            if (doorDelivery.isChecked() == false && pickUp.isChecked() == false) {
                Toast.makeText(getApplicationContext(), "Please choose delivery method", Toast.LENGTH_SHORT).show();
            } else {
                Log.println(Log.ASSERT,"Cash","Cash Selected");
                SUCCESS_Or_Failure = 1;

                DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("complete");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            reference.setValue("1");
                            DatabaseReference databaseReference  =
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("cardorcash");
                            databaseReference.setValue("card");
                        }else {
                            reference.setValue("1");
                            DatabaseReference databaseReference  =
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("cardorcash");
                            databaseReference.setValue("cash");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        reference.setValue("0");
                    }
                });

                startActivity(new Intent(CheckOut.this,SuccessOrFailur.class));
                finish();
            }
        }else if (card.isSelected() || card.isChecked()){
            if (doorDelivery.isChecked() == false && pickUp.isChecked() == false) {
                Toast.makeText(getApplicationContext(), "Please choose delivery method", Toast.LENGTH_SHORT).show();
            } else {
                SUCCESS_Or_Failure = 0;


                DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("complete");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            reference.setValue("1");
                            DatabaseReference databaseReference  =
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("cardorcash");
                            databaseReference.setValue("card");
                        }else {
                            reference.setValue("1");
                            DatabaseReference databaseReference  =
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("cardorcash");
                            databaseReference.setValue("card");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        reference.setValue("0");
                    }
                });

                startActivity(new Intent(CheckOut.this,OnlinePaymeny.class));
                finish();
            }

        }
        else if(card.isChecked()==false && cash.isChecked() == false){
            Toast.makeText(getApplicationContext(), "Please choose payment method", Toast.LENGTH_SHORT).show();
        }
    }

    public void getIntentData(){
        Intent intent = getIntent();
         long x = intent.getExtras().getLong("_price_",0);
        checkoutPriceTotal.setText(""+x);
    }
}