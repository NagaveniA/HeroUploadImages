package com.example.uploadimages.networkUtils.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.repositories.SignInRepo;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.google.gson.JsonObject;

public class SignInViewModel extends ViewModel {
    MutableLiveData<ServerResponse<LoginPojo>> loginBeanMutableLiveData;
    private SignInRepo signInRepo;

    public SignInViewModel(SignInRepo signInRepo) {
        this.signInRepo = signInRepo;
    }

    public void callLoginApi(JsonObject jsonObject) {
        if (loginBeanMutableLiveData != null) {
            return;
        }
        loginBeanMutableLiveData = signInRepo.postLogin(jsonObject);

    }

    public LiveData<ServerResponse<LoginPojo>> getLoginData() {
        return loginBeanMutableLiveData;
    }

}
