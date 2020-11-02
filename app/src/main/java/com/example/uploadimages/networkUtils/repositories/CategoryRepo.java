package com.example.uploadimages.networkUtils.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepo {
    private Api api;

    public CategoryRepo() {
        api = ApiClient.getClient().create(Api.class);
    }

    public MutableLiveData<ServerResponse<ArrayList<CategoryPojo>>> postCategoryList(String token) {
        MutableLiveData<ServerResponse<ArrayList<CategoryPojo>>> categoryBeanMutableData = new MutableLiveData<>();
        api.postCategoryList(token).enqueue(new Callback<ServerResponse<ArrayList<CategoryPojo>>>() {
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<CategoryPojo>>> call, Response<ServerResponse<ArrayList<CategoryPojo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        categoryBeanMutableData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));
                    } else {
                        categoryBeanMutableData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                    }
                } else {
                    categoryBeanMutableData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<ArrayList<CategoryPojo>>> call, Throwable t) {
                categoryBeanMutableData.setValue(ServerResponse.error(t.toString(), null));
            }
        });
        return categoryBeanMutableData;
    }
}
