package com.example.uploadimages.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uploadimages.BaseActivity;
import com.example.uploadimages.R;
import com.example.uploadimages.networkUtils.Api;
import com.example.uploadimages.networkUtils.ApiClient;
import com.example.uploadimages.networkUtils.ApiConstants;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    /**
     * ButterKnife Code
     **/
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_mob_error)
    TextView tvMobError;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_hide)
    ImageView ivHide;
    @BindView(R.id.tv_pass_error)
    TextView tvPassError;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_terms)
    TextView tvTerms;
    /**
     * ButterKnife Code
     **/
    boolean isPasswordVisible = false;
    private LoginSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sessionManager = new LoginSessionManager(getApplicationContext());
        if (sessionManager.isUserLoggedIn()) {
            startActivity(new Intent(this, CategoryActivity.class));
            finish();
        }
        textWatcher();
    }

    private void textWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().toString().length() == 0) {
                    ivHide.setVisibility(View.GONE);
                } else {
                    ivHide.setVisibility(View.VISIBLE);
                }
                tvMobError.setVisibility(View.GONE);
                tvPassError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etName.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.iv_hide, R.id.btn_login})
    public void OnClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_forgot_pass:
//                Intent intent = new Intent(this, ForgotPasswordActivity.class);
//                startActivity(intent);
//                break;
            case R.id.iv_hide:
                if (!isPasswordVisible) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                }
                break;
            case R.id.btn_login:
                if (validate()) {
                    callApi();
                }
                break;
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            tvMobError.setVisibility(View.VISIBLE);
            etName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
            tvPassError.setVisibility(View.VISIBLE);
            etPassword.requestFocus();
            return false;
        } else
            return true;
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        showProgressDialog(this);
        JsonObject body = new JsonObject();
        body.addProperty(ApiConstants.EMAIL, etName.getText().toString());
        body.addProperty(ApiConstants.PASSWORD, etPassword.getText().toString());
        Call<ServerResponse<LoginPojo>> call = api.postsignin(body);
        call.enqueue(new Callback<ServerResponse<LoginPojo>>() {
            @Override
            public void onResponse(Call<ServerResponse<LoginPojo>> call, Response<ServerResponse<LoginPojo>> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        LoginPojo loginBean = response.body().getData();
                        Intent i;
                        sessionManager.createUserSession(loginBean);
                        i = new Intent(LoginActivity.this, CategoryActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
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
            public void onFailure(Call<ServerResponse<LoginPojo>> call, Throwable t) {
                dismissProgressDialog();
                showToast(getApplicationContext(), t.toString());

            }
        });

    }

}