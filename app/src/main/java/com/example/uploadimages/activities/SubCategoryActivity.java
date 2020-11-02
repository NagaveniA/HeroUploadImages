package com.example.uploadimages.activities;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uploadimages.BaseActivity;
import com.example.uploadimages.R;
import com.example.uploadimages.adapter.SubCategoryAdapter;
import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ApiConstants;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.factories.SubCategoryFactory;
import com.example.uploadimages.networkUtils.viewmodel.SubCategoryViewModel;
import com.example.uploadimages.responsepojo.SubCategoryPojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;
import com.example.uploadimages.utils.AppConstants;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends BaseActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.toolbar_top)
    androidx.appcompat.widget.Toolbar toolbarTop;
    @BindView(R.id.ic_back)
    ImageView icBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.rv_subcategory)
    androidx.recyclerview.widget.RecyclerView rvSubcategory;
    /**
     * ButterKnife Code
     **/
    private String category_id, category_name, access_token;
    private LoginSessionManager sessionManager;
    SubCategoryViewModel subCategoryViewModel;
    @Inject
    SubCategoryFactory subCategoryFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catgory);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);
        sessionManager = new LoginSessionManager(this);
        access_token = sessionManager.getUserDetails().get(LoginSessionManager.KEY_Accesstoken);
        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(AppConstants.KEY_CATEGORY_ID) != null) {
                category_id = getIntent().getStringExtra(AppConstants.KEY_CATEGORY_ID);
            }
            if (getIntent().getStringExtra(AppConstants.KEY_CATEGORY_NAME) != null) {
                category_name = getIntent().getStringExtra(AppConstants.KEY_CATEGORY_NAME);
                toolbarTitle.setText(category_name);
            }

        }
        callSubCategoryApi();
    }

    private void callSubCategoryApi() {
        JsonObject body = new JsonObject();
        body.addProperty(ApiConstants.CATEGORY_ID, category_id);
        subCategoryViewModel = new ViewModelProvider(this, subCategoryFactory).get(SubCategoryViewModel.class);
        subCategoryViewModel.callSubCategoryApi(access_token, body);
        subCategoryViewModel.getSubcategoryList().observe(this, this::processSubCategoryList);
    }

    private void processSubCategoryList(ServerResponse<SubCategoryPojo> response) {
        switch (response.statusType) {
            case LOADING:
                showProgressDialog(this);
                break;
            case SUCCESS:
                dismissProgressDialog();
                ArrayList<SubCategoryPojo.Subcategory> subCategoryPojos = new ArrayList<SubCategoryPojo.Subcategory>();
                if (response.getData() != null) {
                    subCategoryPojos = response.getData().getSubcategory();
                    if (subCategoryPojos.size() > 0) {
                        initialiseRecycler(subCategoryPojos);
                    } else {
                        tvNodata.setVisibility(View.VISIBLE);
                    }
                    if (response.getData().getCategoryName() != null) {
                        toolbarTitle.setText(response.getData().getCategoryName());
                    } else {
                        toolbarTitle.setText(category_name);
                    }
                } else {
                    tvNodata.setVisibility(View.VISIBLE);
                }
                break;
            case ERROR:
                dismissProgressDialog();
                showToast(getApplicationContext(), response.getStatusMessage().toLowerCase());

        }
    }

    private void initialiseRecycler(ArrayList<SubCategoryPojo.Subcategory> subCategoryPojos) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rvSubcategory.setLayoutManager(layoutManager);
        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, subCategoryPojos, category_id);
        rvSubcategory.setAdapter(subCategoryAdapter);
    }

    @OnClick({R.id.ic_back})
    public void onClic(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
        }
    }
}