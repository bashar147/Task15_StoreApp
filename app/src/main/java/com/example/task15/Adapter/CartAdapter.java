package com.example.task15.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.task15.Classes.CartModel;
import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private Context context;
    private ArrayList<CartModel> cartModels;
    public long xxx;
    public CartAdapter(Context context, ArrayList<CartModel> cartModels) {
        this.context = context;
        this.cartModels = cartModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler, parent, false);
        return new CartAdapter.ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(""+cartModels.get(position).getName());
        holder.price.setText(""+cartModels.get(position).getCART_PRICE());
        holder.counter.setText(""+cartModels.get(position).getCartCounter());

        Glide.with(context).load(cartModels.get(position).getImageUrl()).into(holder.cartImage);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = context.getSharedPreferences("category counter", MODE_PRIVATE);
                prefs.getLong(cartModels.get(position).getName(),1);
                long count1 = prefs.getLong(cartModels.get(position).getName(),1);
                count1++;
                holder.counter.setText(""+count1);

                DatabaseReference databaseReference
                        =FirebaseDatabase.getInstance().getReference("orders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(cartModels.get(position).getName())
                        .child("cartCounter");

                databaseReference.setValue(count1);

                SharedPreferences.Editor editor = context.getSharedPreferences("category counter", MODE_PRIVATE).edit();
                editor.putLong(cartModels.get(position).getName(), count1);
                editor.apply();

            }
        });

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int count1 = Integer.parseInt(holder.counter.getText().toString());
//                count1 = count1-1;
//                if (count1==0){
//
//                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("order");
//
//                    reference1.setValue(cartModels.size()-1);
//
//                    deleteItem(position,holder);
//                    DatabaseReference databaseReference =
//                            FirebaseDatabase.getInstance().getReference("orders")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .child("order"+(position+1));
//                    databaseReference.setValue(null);
//                }
//                holder.counter.setText(""+count1);

                SharedPreferences prefs = context.getSharedPreferences("category counter", MODE_PRIVATE);
                prefs.getLong(cartModels.get(position).getName(),1);
                long count1 = prefs.getLong(cartModels.get(position).getName(),1);
                if (count1>0)
                {
                count1--;
                holder.counter.setText(""+count1);

                DatabaseReference databaseReference
                        =FirebaseDatabase.getInstance().getReference("orders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(cartModels.get(position).getName())
                        .child("cartCounter");

                databaseReference.setValue(count1);

                SharedPreferences.Editor editor = context.getSharedPreferences("category counter", MODE_PRIVATE).edit();
                editor.putLong(cartModels.get(position).getName(), count1);
                editor.apply();


            }else if (count1==0){
                    deleteItem(position,holder);
                    holder.relativeLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cartImage;
        TextView name , price , counter ;
        ImageButton plus , del ;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cartImage);
            name = itemView.findViewById(R.id.cartName);
            price = itemView.findViewById(R.id.cartPriceText);
            counter = itemView.findViewById(R.id.cartCounterText);

            plus = itemView.findViewById(R.id.cartPlusImageBtn);
            del = itemView.findViewById(R.id.cartDelImageBtn);

            relativeLayout = itemView.findViewById(R.id.cartRelativeParent);
        }
    }


    private void deleteItem(int position , @NonNull CartAdapter.ViewHolder holder) {
        cartModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartModels.size());
        holder.relativeLayout.setVisibility(View.GONE);
    }
}
