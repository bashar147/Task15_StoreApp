package com.example.task15.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.task15.CategoryActivity.CategoryActivity;
import com.example.task15.CategoryActivity.WomenActivity;
import com.example.task15.Classes.CategoryClick;
import com.example.task15.Classes.CategoryModel;
import com.example.task15.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    Context context;
    ArrayList<CategoryModel> categoryModels;
    ArrayList<CategoryModel> hCategoryModelsCopy ;
    public static int size =0;
    public SearchAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
        hCategoryModelsCopy = new ArrayList<>();
        hCategoryModelsCopy.addAll(categoryModels);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycler, parent, false);
        return new SearchAdapter.ViewHolder(inflate);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(""+categoryModels.get(position).getName());
        holder.price.setText(""+categoryModels.get(position).getPRICE());
        holder.counter.setText(""+categoryModels.get(position).getCOUNTER());
        Glide.with(context).load(categoryModels.get(position).getImageUrl()).into(holder.searchImage);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        ImageView searchImage;
        TextView name , price , counter ;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.searchName);
            price = itemView.findViewById(R.id.searchPriceText);
            counter = itemView.findViewById(R.id.searchCounterText);
            searchImage = itemView.findViewById(R.id.searchImage);
            relativeLayout = itemView.findViewById(R.id.searchRelativeParent);
        }
    }

    public void search (@NonNull CharSequence charSequence){
        ArrayList<CategoryModel>  arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(charSequence)){
            for (CategoryModel data : categoryModels){
                if (data.getName().toLowerCase().contains(charSequence)){
                    arrayList.add(data);
                    size = arrayList.size();
                }
            }

        }else {
            arrayList.addAll(hCategoryModelsCopy);

        }

        categoryModels.clear();
        categoryModels.addAll(arrayList);
        notifyDataSetChanged();
        arrayList.clear();
    }
}

