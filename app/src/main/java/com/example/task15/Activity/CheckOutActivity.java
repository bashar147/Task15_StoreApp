package com.example.task15.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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

import org.w3c.dom.Text;

public class CheckOutActivity extends AppCompatActivity {

    private TextView change ,priceText;
    private EditText    name , distance , phoneNumber , radioText;
    private RadioButton doorDelivery , pickUp;
    private Button proceed_to_payment;
    public long _price_;
    private ImageView checkOutClickBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        initialize();

    }

    public void initialize(){
        getData();
        change = findViewById(R.id.changeTExt);
        change.setOnClickListener(this::ChangeClick);
        name = findViewById(R.id.checkOutNameEditText);
        distance = findViewById(R.id.checkOutDistanceEditText);
        phoneNumber = findViewById(R.id.checOutPhoneEditText);

        radioText = findViewById(R.id.doorDeleveryCheclBox);

        priceText = findViewById(R.id.checkoutTotal);

        doorDelivery = findViewById(R.id.doorRadio);
        pickUp = findViewById(R.id.pickUpRadioBox);

        checkOutClickBack = findViewById(R.id.checkOutClickBackcheck);
        checkOutClickBack.setOnClickListener(this::checkOutClickBackcheck);
        proceed_to_payment = findViewById(R.id.checkOutProceed_to_payment);

        proceed_to_payment.setOnClickListener(this:: buttonClick);
        getIntentData();
    }

    private void ChangeClick(View view) {
        name.setEnabled(true);
        distance.setEnabled(true);
        phoneNumber.setEnabled(true);

    }

    private void checkOutClickBackcheck(View view) {
        finish();
    }

    private void buttonClick(View view) {
        if (doorDelivery.isChecked() == false && pickUp.isChecked() == false) {
            Toast.makeText(getApplicationContext(), "Please choose delivery method", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent  = new Intent(getApplicationContext(),CheckOut.class);
            intent.putExtra("_price_",_price_);
            startActivity(intent);
            finish();
        }
    }
   public void getData(){
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
               .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   name.setText(""+snapshot.child("name").getValue(String.class));
                   distance.setText(""+snapshot.child("location").getValue(String.class));
                   phoneNumber.setText(" "+snapshot.child("phoneNumber").getValue(String.class));

               }else {
                   Toast.makeText(getApplicationContext(), "Failed To Get Data", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }

   public void getIntentData(){
        Intent intent = getIntent();
       _price_ = intent.getExtras().getLong("_price_",0);
        priceText.setText(""+intent.getExtras().getLong("_price_",0));
   }
}