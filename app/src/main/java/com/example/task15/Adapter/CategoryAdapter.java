package com.example.task15.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.task15.CategoryActivity.CategoryActivity;
import com.example.task15.Classes.CategoryClick;
import com.example.task15.Classes.CategoryModel;
import com.example.task15.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    ArrayList<CategoryModel>  categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.catergory_recycler, parent, false);
        return new CategoryAdapter.ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(categoryModels.get(position).getName());
        holder.price.setText(""+categoryModels.get(position).getPRICE());
        holder.counter.setText(""+categoryModels.get(position).getCOUNTER());
        Glide.with(context).load(categoryModels.get(position).getImageUrl()).into(holder.categoryImage);

        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("name",categoryModels.get(position).getName());
                intent.putExtra("url",categoryModels.get(position).getImageUrl());
                intent.putExtra("price",categoryModels.get(position).getPRICE());
                intent.putExtra("counter",categoryModels.get(position).getCOUNTER());
                intent.putExtra("madeIn",categoryModels.get(position).getMadIn());
                intent.putExtra("description",categoryModels.get(position).getDescription());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoryModels.size();
    }



    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView name , price , counter ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.categoryName);
            price = itemView.findViewById(R.id.categoryPriceText);
            counter = itemView.findViewById(R.id.counterCategoryText);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
