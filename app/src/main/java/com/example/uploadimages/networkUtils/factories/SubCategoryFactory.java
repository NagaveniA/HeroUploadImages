package com.example.uploadimages.networkUtils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.networkUtils.repositories.SignInRepo;
import com.example.uploadimages.networkUtils.repositories.SubCategoryRepo;
import com.example.uploadimages.networkUtils.viewmodel.SignInViewModel;
import com.example.uploadimages.networkUtils.viewmodel.SubCategoryViewModel;

public class SubCategoryFactory implements ViewModelProvider.Factory {
    SubCategoryRepo subCategoryRepo;

    public SubCategoryFactory(SubCategoryRepo subCategoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SubCategoryViewModel.class)) {
            return (T) new SubCategoryViewModel(subCategoryRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
