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

import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.BaseActivity;
import com.example.uploadimages.R;
import com.example.uploadimages.networkUtils.ApiConstants;
import com.example.uploadimages.networkUtils.ServerResponse;
import com.example.uploadimages.networkUtils.factories.SignInFactory;
import com.example.uploadimages.networkUtils.viewmodel.SignInViewModel;
import com.example.uploadimages.responsepojo.LoginPojo;
import com.example.uploadimages.sessionManager.LoginSessionManager;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

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

    SignInViewModel signInViewModel;
    @Inject
    SignInFactory signInFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AndroidInjection.inject(this);
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
        JsonObject body = new JsonObject();
        body.addProperty(ApiConstants.EMAIL, etName.getText().toString());
        body.addProperty(ApiConstants.PASSWORD, etPassword.getText().toString());
        signInViewModel = new ViewModelProvider(this, signInFactory).get(SignInViewModel.class);
        signInViewModel.callLoginApi(body);
        signInViewModel.getLoginData().observe(this, this::processSignInApi);
    }

    private void processSignInApi(ServerResponse<LoginPojo> response) {
        switch (response.statusType) {
            case LOADING:
                showProgressDialog(this);
                break;
            case SUCCESS:
                dismissProgressDialog();
                if (response.getData() != null) {
                    LoginPojo loginBean = response.getData();
                    Intent i;
                    sessionManager.createUserSession(loginBean);
                    i = new Intent(LoginActivity.this, CategoryActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                break;
            case ERROR:
                dismissProgressDialog();
                showToast(getApplicationContext(), response.getStatusMessage().toLowerCase());
        }
    }
}