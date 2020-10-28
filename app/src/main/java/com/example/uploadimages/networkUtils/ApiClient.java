package com.example.uploadimages.networkUtils;

/**
 * Created by Codebele on 08-Aug-19.
 */


import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;
    private static Context contextl;
    public static final String BASE_URL ="https://codebele.com/app_hero/Api/";
    public static final String IMAGE_URL ="https://codebele.com/app_hero/";

    public static Retrofit getClient() {
//        sessionManager =new  ActivitySessionManager(contextl);
//        base_url = sessionManager.getBaseUrl();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .readTimeout(500, TimeUnit.SECONDS)
                .connectTimeout(500, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.SECONDS)// write timeout
                .addInterceptor((Interceptor) new NetworkInterceptor())
                .addInterceptor(interceptor)
                .build();

//
//            if (retrofit==null) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }


}
