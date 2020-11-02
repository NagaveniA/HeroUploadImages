package com.example.uploadimages.networkUtils.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.repositories.CategoryRepo;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.responsepojo.LoginPojo;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {
    private CategoryRepo categoryRepo;
    MutableLiveData<ServerResponse<ArrayList<CategoryPojo>>> categoryBeanMutableLiveData;

    public CategoryViewModel(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public void callCategoryListApi(String token){
        if (categoryBeanMutableLiveData != null) {
            return;
        }
        categoryBeanMutableLiveData = categoryRepo.postCategoryList(token);

    }
    public LiveData<ServerResponse<ArrayList<CategoryPojo>>> getCategoryList() {
        return categoryBeanMutableLiveData;
    }
}
