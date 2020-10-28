package com.example.uploadimages.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uploadimages.R;
import com.example.uploadimages.activities.SubCategoryActivity;
import com.example.uploadimages.activities.UploadImageActivity;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.example.uploadimages.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SubCategoryPojo.Subcategory> subCategoryPojos = new ArrayList<>();
    private String category_id;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryPojo.Subcategory> subCategoryPojos, String category_id) {
        this.context = context;
        this.subCategoryPojos = subCategoryPojos;
        this.category_id = category_id;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_subcategory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyViewHolder holder, int position) {
        SubCategoryPojo.Subcategory subCategoryPojo = subCategoryPojos.get(position);
        if (subCategoryPojo.getSubcategoryName() != null) {
            holder.tvSubCategory.setText(subCategoryPojo.getSubcategoryName());
        }
        if (subCategoryPojo.getSubCategoryImage() != null) {
            Glide.with(context)
                    .load(subCategoryPojo.getSubCategoryImage()).
                    placeholder(R.drawable.hero_logo).
                    into(holder.ivSubCategory);
        }

    }

    @Override
    public int getItemCount() {
        if (subCategoryPojos.size() > 0)
            return subCategoryPojos.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife Code
         **/
        @BindView(R.id.iv_sub_category)
        ImageView ivSubCategory;
        @BindView(R.id.tv_sub_category)
        TextView tvSubCategory;
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
                    SubCategoryPojo.Subcategory subCategoryPojo = subCategoryPojos.get(pos);
                    Intent intent = new Intent(context, UploadImageActivity.class);
                    intent.putExtra(AppConstants.KEY_CATEGORY_ID, category_id);
                    intent.putExtra(AppConstants.KEY_SUBCATEGORY_ID, subCategoryPojo.getSubcategoryId());
                    intent.putExtra(AppConstants.KEY_SUBCATEGORY_NAME, subCategoryPojo.getSubcategoryName());
                    context.startActivity(intent);
                }
            });
        }
    }
}
