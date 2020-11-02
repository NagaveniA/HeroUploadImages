package com.example.uploadimages.networkUtils.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepo {
    private static SignInRepo signInRepo;
    private Api api;

    public SignInRepo() {
        api = ApiClient.getClient().create(Api.class);
    }

    public MutableLiveData<ServerResponse<LoginPojo>> postLogin(JsonObject jsonObject) {
        MutableLiveData<ServerResponse<LoginPojo>> loginBeanMutableLiveData = new MutableLiveData<>();
        api.postsignin(jsonObject).enqueue(new Callback<ServerResponse<LoginPojo>>() {
            @Override
            public void onResponse(Call<ServerResponse<LoginPojo>> call, Response<ServerResponse<LoginPojo>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    if (response.body().isStatus()) {
                        loginBeanMutableLiveData.setValue(ServerResponse.success(response.body().getData(), response.body().getStatusMessage()));

                    } else {
                        loginBeanMutableLiveData.setValue(ServerResponse.error(response.body().getStatusMessage(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LoginPojo>> call, Throwable t) {
                loginBeanMutableLiveData.setValue(ServerResponse.error(t.toString(), null));

            }
        });
        return loginBeanMutableLiveData;
    }
}
