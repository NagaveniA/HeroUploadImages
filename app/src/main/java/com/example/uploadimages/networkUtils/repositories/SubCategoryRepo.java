package com.example.uploadimages.networkUtils.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryRepo {
    private static SubCategoryRepo subCategoryRepo;
    private Api api;
    public SubCategoryRepo() {
        api = ApiClient.getClient().create(Api.class);
    }

    public MutableLiveData<ServerResponse<SubCategoryPojo>> postSubCategoryList(String token,JsonObject jsonObject) {
        MutableLiveData<ServerResponse<SubCategoryPojo>> subCategoryBeanMutableLiveData = new MutableLiveData<>();
        api.postSubCategoryList(token,jsonObject).enqueue(new Callback<ServerResponse<SubCategoryPojo>>() {
            @Override
            public void onResponse(Call<ServerResponse<SubCategoryPojo>> call, Response<ServerResponse<SubCategoryPojo>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    if (response.body().isStatus()) {
                        subCategoryBeanMutableLiveData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));

                    } else {
                        subCategoryBeanMutableLiveData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<SubCategoryPojo>> call, Throwable t) {
                subCategoryBeanMutableLiveData.setValue(ServerResponse.error(t.toString(), null));

            }
        });
        return subCategoryBeanMutableLiveData;
    }
}
