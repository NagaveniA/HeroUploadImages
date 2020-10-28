package com.example.uploadimages.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uploadimages.R;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.responsepojo.UploadimagePojo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadmageAdapter extends RecyclerView.Adapter<UploadmageAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<UploadimagePojo> uploadimagePojos = new ArrayList<>();

    public UploadmageAdapter(Context context, ArrayList<UploadimagePojo> uploadimagePojos) {
        this.context = context;
        this.uploadimagePojos = uploadimagePojos;
    }

    @NonNull
    @Override
    public UploadmageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UploadmageAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UploadmageAdapter.MyViewHolder holder, int position) {
        UploadimagePojo uploadimagePojo = uploadimagePojos.get(position);
        if (uploadimagePojo.getImage() != null) {
            Glide.with(context)
                    .load(ApiClient.IMAGE_URL +uploadimagePojo.getImage()).
                    placeholder(R.drawable.hero_logo).
                    into(holder.ivResponseImage);
        }


    }

    @Override
    public int getItemCount() {
        if (uploadimagePojos.size() > 0)
            return uploadimagePojos.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.iv_response_image)
        ImageView ivResponseImage;

        /**
         * ButterKnife Code
         **/
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
