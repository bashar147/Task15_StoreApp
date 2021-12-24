package com.example.task15.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task15.Classes.OrderModel;
import com.example.task15.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderModel> models;

    public OrderAdapter(Context context, ArrayList<OrderModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_recycler, parent, false);
        return new OrderAdapter.ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        holder.price.setText(models.get(position).getPrice());
        holder.product.setText(models.get(position).getProduct());
        holder.complete.setText(models.get(position).getComp());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position,holder);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.setValue(null);
            }
        });
   }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product, complete, price , delete;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product = itemView.findViewById(R.id.orderProducteditText);
            price = itemView.findViewById(R.id.orderPriceEditText);
            complete = itemView.findViewById(R.id.orderCcompleteText);
            delete = itemView.findViewById(R.id.deleteTExt);

            relativeLayout = itemView.findViewById(R.id.parentRelativeLayout);


        }
    }

    private void deleteItem(int position , @NonNull OrderAdapter.ViewHolder holder) {
        models.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, models.size());
        holder.relativeLayout.setVisibility(View.GONE);
    }

}
