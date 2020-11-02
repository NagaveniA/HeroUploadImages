package com.example.uploadimages.networkUtils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.networkUtils.repositories.SignInRepo;
import com.example.uploadimages.networkUtils.viewmodel.SignInViewModel;

public class SignInFactory implements ViewModelProvider.Factory {
    SignInRepo signInRepo;

    public SignInFactory(SignInRepo signInRepo) {
        this.signInRepo = signInRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(signInRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
