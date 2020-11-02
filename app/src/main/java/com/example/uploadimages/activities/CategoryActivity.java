package com.example.uploadimages.activities;

import androidx.lifecycle.ViewModelProvider;
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
import com.example.uploadimages.networkUtils.factories.CategoryFactory;
import com.example.uploadimages.networkUtils.viewmodel.CategoryViewModel;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.toolbar_top)
    androidx.appcompat.widget.Toolbar toolbarTop;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.rv_category)
    androidx.recyclerview.widget.RecyclerView rvCategory;
    /**
     * ButterKnife Code
     **/
    private LoginSessionManager sessionManager;
    private String access_token;
    private CategoryAdapter categoryAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Inject
    CategoryFactory categoryFactory;
    CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);
        sessionManager = new LoginSessionManager(this);
        access_token = sessionManager.getUserDetails().get(LoginSessionManager.KEY_Accesstoken);
        callCategoryApi();
    }

    private void callCategoryApi() {
        categoryViewModel = new ViewModelProvider(this, categoryFactory).get(CategoryViewModel.class);
        categoryViewModel.callCategoryListApi(access_token);
        categoryViewModel.getCategoryList().observe(this, this::processCategoryList);
    }

    private void processCategoryList(ServerResponse<ArrayList<CategoryPojo>> response) {
        switch (response.statusType) {
            case LOADING:
                showProgressDialog(this);
                break;
            case SUCCESS:
                dismissProgressDialog();
                if (response.getData() != null) {
                    ArrayList<CategoryPojo> categoryPojos = new ArrayList<>();
                    categoryPojos = response.getData();
                    if (categoryPojos.size() > 0) {
                        initialiseRecycler(categoryPojos);
                    } else {
                        tvNodata.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case ERROR:
                dismissProgressDialog();
                showToast(getApplicationContext(),response.getStatusMessage());

        }
    }

    private void initialiseRecycler(ArrayList<CategoryPojo> categoryPojos) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
        categoryAdapter = new CategoryAdapter(this, categoryPojos);
        rvCategory.setAdapter(categoryAdapter);

    }
}