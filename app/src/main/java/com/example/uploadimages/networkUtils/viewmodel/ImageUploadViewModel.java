package com.example.uploadimages.networkUtils.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.factories.ImageUploadFactory;
import com.example.uploadimages.networkUtils.repositories.ImageUploadRepo;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.responsepojo.UploadimagePojo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ImageUploadViewModel extends ViewModel {
    MutableLiveData<ServerResponse<ArrayList<UploadimagePojo>>> uploadImageBeanMutableLiveData;
    ImageUploadRepo imageUploadRepo;

    public ImageUploadViewModel(ImageUploadRepo imageUploadRepo) {
        this.imageUploadRepo = imageUploadRepo;
    }

    public void callUploadImageApi(String content_type, JsonObject jsonObject) {
        if (uploadImageBeanMutableLiveData != null) {
            return;
        }
        uploadImageBeanMutableLiveData = imageUploadRepo.postUploadImage(content_type, jsonObject);

    }

    public LiveData<ServerResponse<ArrayList<UploadimagePojo>>> getImageUploadData() {
        return uploadImageBeanMutableLiveData;
    }

}
