package com.example.uploadimages.networkUtils;

import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.example.uploadimages.responsepojo.UploadimagePojo;
import com.example.uploadimages.utils.AppConstants;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Codebele on 07-May-19.
 */
public interface Api {
    @POST("sigin")
    Call<ServerResponse<LoginPojo>> postsignin(@Body JsonObject body);

    @POST("category-list")
    Call<ServerResponse<ArrayList<CategoryPojo>>> postCategoryList(@Header(AppConstants.KEY_ACCESS_TOKEN) String access_token);

    @POST("subcategory-list")
    Call<ServerResponse<SubCategoryPojo>> postSubCategoryList(@Header(AppConstants.KEY_ACCESS_TOKEN) String access_token, @Body JsonObject jsonObject);

    @POST("image-upload")
    Call<ServerResponse<ArrayList<UploadimagePojo>>> postUploadImage(@Header(AppConstants.KEY_ACCESS_TOKEN) String access_token, @Body JsonObject jsonObject);

}

