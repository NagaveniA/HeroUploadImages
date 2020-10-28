package com.example.uploadimages.responsepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryPojo implements Serializable {
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("subcategory")
    @Expose
    private ArrayList<Subcategory> subcategory = null;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }
    public class Subcategory implements Serializable{

        @SerializedName("subcategory_id")
        @Expose
        private String subcategoryId;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("subCategory_image")
        @Expose
        private Object subCategoryImage;

        public String getSubcategoryId() {
            return subcategoryId;
        }

        public void setSubcategoryId(String subcategoryId) {
            this.subcategoryId = subcategoryId;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public Object getSubCategoryImage() {
            return subCategoryImage;
        }

        public void setSubCategoryImage(Object subCategoryImage) {
            this.subCategoryImage = subCategoryImage;
        }

    }

}
