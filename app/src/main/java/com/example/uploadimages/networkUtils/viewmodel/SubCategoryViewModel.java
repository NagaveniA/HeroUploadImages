package com.example.uploadimages.networkUtils.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.repositories.SubCategoryRepo;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.google.gson.JsonObject;

public class SubCategoryViewModel extends ViewModel {
    MutableLiveData<ServerResponse<SubCategoryPojo>> subcategoryBeanMutableLiveData;
    private SubCategoryRepo subCategoryRepo;

    public SubCategoryViewModel(SubCategoryRepo subCategoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
    }

    public void callSubCategoryApi(String token, JsonObject jsonObject) {
        if (subcategoryBeanMutableLiveData != null) {
            return;
        }
        subcategoryBeanMutableLiveData = subCategoryRepo.postSubCategoryList(token, jsonObject);

    }

    public LiveData<ServerResponse<SubCategoryPojo>> getSubcategoryList() {
        return subcategoryBeanMutableLiveData;
    }
}
