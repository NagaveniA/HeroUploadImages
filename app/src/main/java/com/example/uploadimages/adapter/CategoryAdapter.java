package com.example.uploadimages.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uploadimages.R;
import com.example.uploadimages.activities.SubCategoryActivity;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<CategoryPojo> categoryPojos = new ArrayList<>();

    public CategoryAdapter(Context context, ArrayList<CategoryPojo> categoryPojos) {
        this.context = context;
        this.categoryPojos = categoryPojos;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        CategoryPojo categoryPojo = categoryPojos.get(position);
        if (categoryPojo.getCategoryName() != null) {
            holder.tvCategoryName.setText(categoryPojo.getCategoryName());
        }
        if (categoryPojo.getCategoryImage() != null) {
            Glide.with(context)
                    .load(categoryPojo.getCategoryName()).
                    placeholder(R.drawable.hero_logo).
                    into(holder.ivBanner);
        }
        holder.tvProgressNo.setText("45%");

    }

    @Override
    public int getItemCount() {
        if (categoryPojos.size() > 0)
        return categoryPojos.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.rr_image)
        RelativeLayout rrImage;
        @BindView(R.id.iv_banner)
        ImageView ivBanner;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_progress_no)
        TextView tvProgressNo;
        @BindView(R.id.pBar)
        ProgressBar pBar;

        /**
         * ButterKnife Code
         **/
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    CategoryPojo categoryPojo = categoryPojos.get(pos);
                    Intent intent = new Intent(context, SubCategoryActivity.class);
                    intent.putExtra(AppConstants.KEY_CATEGORY_ID, categoryPojo.getCategoryId());
                    intent.putExtra(AppConstants.KEY_CATEGORY_NAME, categoryPojo.getCategoryName());
                    context.startActivity(intent);

                }
            });
        }
    }
}
