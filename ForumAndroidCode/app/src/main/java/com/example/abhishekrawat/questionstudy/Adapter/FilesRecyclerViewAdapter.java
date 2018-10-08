package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.abhishekrawat.questionstudy.Model.MediaFilesDTO;
import com.example.abhishekrawat.questionstudy.R;

import java.util.List;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.FileViewHolder> implements View.OnClickListener {

    boolean isImageFitToScreen=false;
    private List<MediaFilesDTO> filesList;
    Context mContext;


    public FilesRecyclerViewAdapter(Context context, List<MediaFilesDTO> files) {
        this.mContext = context;
        this.filesList = files;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_files, parent, false);
        FileViewHolder vh = new FileViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        MediaFilesDTO file = filesList.get(position);
        CircularProgressDrawable circularProgressDrawable =new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5);
        circularProgressDrawable.setCenterRadius(30);
        circularProgressDrawable.start();
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(circularProgressDrawable);
            Glide.with(mContext)
                    .load(file.url)
                    .apply(requestOptions)
                    .into(holder.image);

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
        } catch (Exception ex) {
        }
    }
    @Override
    public int getItemCount() {
        return filesList.size();
    }

    @Override
    public void onClick(View v) {
        if(isImageFitToScreen) {
            isImageFitToScreen=false;
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            (ImageView)v.setAdjustViewBounds(true);
        }else{
            isImageFitToScreen=true;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
    public class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public FileViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);


        }
    }
}
