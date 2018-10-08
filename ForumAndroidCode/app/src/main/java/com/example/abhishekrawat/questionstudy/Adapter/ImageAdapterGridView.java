package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abhishekrawat.questionstudy.R;

import java.util.List;

public class ImageAdapterGridView extends RecyclerView.Adapter<ImageAdapterGridView.GridViewHolder> {
    private Context mContext;
    List<Uri>  mImageUris;
    public ImageAdapterGridView(Context c,List<Uri> imageUris) {
        mImageUris=imageUris;
        mContext = c;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView mImageView;
        mImageView = new ImageView(mContext);
        mImageView.setId(R.id.image);
        mImageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setCropToPadding(true);
        mImageView.setPadding(20, 20, 20, 20);
        GridViewHolder vh=new GridViewHolder(mImageView);
         return vh;
    }

    @Override
    public void onBindViewHolder( GridViewHolder holder, int position) {
            holder.image.setImageURI(mImageUris.get(position));
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mImageUris.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}