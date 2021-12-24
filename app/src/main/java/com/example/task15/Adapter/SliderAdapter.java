package com.example.task15.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.task15.Classes.SliderItem;
import com.example.task15.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context, ArrayList<SliderItem> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(mSliderItems.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class ViewHolder extends SliderViewAdapter.ViewHolder {
        View mView ;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_Image);
            this.mView = itemView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
