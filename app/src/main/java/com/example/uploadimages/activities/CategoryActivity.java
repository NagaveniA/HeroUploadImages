package com.example.uploadimages.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.uploadimages.BaseActivity;
import com.example.uploadimages.R;
import com.example.uploadimages.adapter.CategoryAdapter;
import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity {
    /** ButterKnife Code **/
    @BindView(R.id.toolbar_top)
    androidx.appcompat.widget.Toolbar toolbarTop;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.rv_category)
    androidx.recyclerview.widget.RecyclerView rvCategory;
    /** ButterKnife Code **/
    private LoginSessionManager sessionManager;
    private String access_token;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        sessionManager = new LoginSessionManager(this);
        access_token = sessionManager.getUserDetails().get(LoginSessionManager.KEY_Accesstoken);
        callCategoryApi();
    }

    private void callCategoryApi() {
        Api api = ApiClient.getClient().create(Api.class);
        showProgressDialog(this);
        Call<ServerResponse<ArrayList<CategoryPojo>>> call = api.postCategoryList(access_token);
        call.enqueue(new Callback<ServerResponse<ArrayList<CategoryPojo>>>() {
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<CategoryPojo>>> call, Response<ServerResponse<ArrayList<CategoryPojo>>> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<CategoryPojo> categoryPojos = new ArrayList<>();
                        categoryPojos = response.body().getData();
                        if (categoryPojos.size()>0){
                            initialiseRecycler(categoryPojos);
                        }else {
                            tvNodata.setVisibility(View.VISIBLE);
                        }

                    } else {
                        showToast(getApplicationContext(), response.body().getStatusMessage());
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        showToast(getApplicationContext(), jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast(getApplicationContext(), getResources().getString(R.string.something_wrong));

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<ServerResponse<ArrayList<CategoryPojo>>> call, Throwable t) {
                dismissProgressDialog();
                showToast(getApplicationContext(), t.toString());
            }
        });
    }

    private void initialiseRecycler(ArrayList<CategoryPojo> categoryPojos) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
        categoryAdapter = new CategoryAdapter(this,categoryPojos);
        rvCategory.setAdapter(categoryAdapter);

    }
}