package com.example.uploadimages.activities;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uploadimages.BaseActivity;
import com.example.uploadimages.R;
import com.example.uploadimages.adapter.CategoryAdapter;
import com.example.uploadimages.adapter.UploadmageAdapter;
import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ApiConstants;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.CategoryPojo;
import com.example.uploadimages.responsepojo.UploadimagePojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;
import com.example.uploadimages.utils.AppConstants;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends BaseActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.toolbar_top)
    androidx.appcompat.widget.Toolbar toolbarTop;
    @BindView(R.id.ic_back)
    ImageView icBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ll_image)
    LinearLayout llImage;
    @BindView(R.id.iv_captured_image)
    ImageView ivCapturedImage;
    @BindView(R.id.btn_up_load)
    Button btnUpLoad;
    @BindView(R.id.rv_images)
    androidx.recyclerview.widget.RecyclerView rvImages;
    @BindView(R.id.fab)
    com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    /**
     * ButterKnife Code
     **/
    private String category_id, category_name, access_token, subcategory_name, subcategory_id;
    private LoginSessionManager sessionManager;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_PERMISSIONS = 1;
    private double latitude, longitude;
    String img = "";
    private File path;
    private File savedPhoto;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        ButterKnife.bind(this);
        sessionManager = new LoginSessionManager(this);
        access_token = sessionManager.getUserDetails().get(LoginSessionManager.KEY_Accesstoken);
        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(AppConstants.KEY_CATEGORY_ID) != null) {
                category_id = getIntent().getStringExtra(AppConstants.KEY_CATEGORY_ID);
            }
            if (getIntent().getStringExtra(AppConstants.KEY_SUBCATEGORY_NAME) != null) {
                subcategory_name = getIntent().getStringExtra(AppConstants.KEY_SUBCATEGORY_NAME);
                toolbarTitle.setText(subcategory_name);
            }
            if (getIntent().getStringExtra(AppConstants.KEY_SUBCATEGORY_ID) != null) {
                subcategory_id = getIntent().getStringExtra(AppConstants.KEY_SUBCATEGORY_ID);
            }

        }
        init();
        callUploadImageApi(img);
    }

    @OnClick({R.id.fab, R.id.ic_back, R.id.btn_up_load})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.fab:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                        Calendar cal = Calendar.getInstance();
                        File file = new File(Environment.getExternalStorageDirectory(), (cal.getTimeInMillis() + ".jpg"));
                        Log.e("file", String.valueOf(file));
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }
                break;
            case R.id.btn_up_load:
                callUploadImageApi(img);
                break;
        }


    }

    @Override
    public void updateLocation(Location mCurrentLocation) {
        super.updateLocation(mCurrentLocation);
        try {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    try {
                        onCaptureImageResult(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        } else {

        }
    }

    private void onCaptureImageResult(Intent data) {
        if (data.getExtras() != null) {
            llImage.setVisibility(View.VISIBLE);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            img = "data:image/png;base64," + AppConstants.getStringImage(bitmap);
            Log.v(TAG, "base64" + img);
            ivCapturedImage.setImageBitmap(bitmap);

            File f = new File(Environment.getExternalStorageDirectory(), getFileName());
            path = savedPhoto;
            try {
                f.createNewFile();
                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = f;
            Log.e("filepath", String.valueOf(path));


        }
    }

    private void callUploadImageApi(String img) {
        Api api = ApiClient.getClient().create(Api.class);
        showProgressDialog(this);
        JsonObject body = new JsonObject();
        body.addProperty(ApiConstants.LATITUDE, latitude);
        body.addProperty(ApiConstants.LONGITUDE, longitude);
        body.addProperty(ApiConstants.CATEGORY_ID, category_id);
        body.addProperty(ApiConstants.SUBCATEGORY_ID, subcategory_id);
        body.addProperty(ApiConstants.IMAGE, img);
        Call<ServerResponse<ArrayList<UploadimagePojo>>> call = api.postUploadImage(access_token, body);
        call.enqueue(new Callback<ServerResponse<ArrayList<UploadimagePojo>>>() {
            @Override
            public void onResponse(Call<ServerResponse<ArrayList<UploadimagePojo>>> call, Response<ServerResponse<ArrayList<UploadimagePojo>>> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        ArrayList<UploadimagePojo> uploadimagePojos = new ArrayList<>();
                        uploadimagePojos = response.body().getData();
                        if (uploadimagePojos.size() > 0) {
                            llImage.setVisibility(View.GONE);
                            initialiseRecycler(uploadimagePojos);
                        } else {
//                            tvNodata.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<ServerResponse<ArrayList<UploadimagePojo>>> call, Throwable t) {
                dismissProgressDialog();
                showToast(getApplicationContext(), t.toString());
            }
        });
    }

    private void initialiseRecycler(ArrayList<UploadimagePojo> uploadimagePojos) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
        rvImages.setLayoutManager(staggeredGridLayoutManager);
        UploadmageAdapter uploadmageAdapter = new UploadmageAdapter(this, uploadimagePojos);
        rvImages.setAdapter(uploadmageAdapter);
    }
}