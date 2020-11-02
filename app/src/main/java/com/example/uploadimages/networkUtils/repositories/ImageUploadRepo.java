package com.example.uploadimages.networkUtils.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.UploadimagePojo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploadRepo {
    private static ImageUploadRepo imageUploadRepo;
    private Api api;

    public ImageUploadRepo() {
        api = ApiClient.getClient().create(Api.class);
    }

    public MutableLiveData<ServerResponse<ArrayList<UploadimagePojo>>> postUploadImage(String token, JsonObject jsonObject) {
        MutableLiveData<ServerResponse<ArrayList<UploadimagePojo>>> uploadImageBeanMutableData = new MutableLiveData<>();
        api.postUploadImage(token, jsonObject).enqueue(new Callback<ServerResponse<ArrayList<UploadimagePojo>>>() {
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<UploadimagePojo>>> call, Response<ServerResponse<ArrayList<UploadimagePojo>>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        uploadImageBeanMutableData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));


                    } else {
                        uploadImageBeanMutableData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                    }

                }
            }

            @Override
            public void onFailure
                    (Call<ServerResponse<ArrayList<UploadimagePojo>>> call, Throwable t) {
                uploadImageBeanMutableData.setValue(ServerResponse.error(t.toString(), null));

            }
        });
        return uploadImageBeanMutableData;
    }

}
