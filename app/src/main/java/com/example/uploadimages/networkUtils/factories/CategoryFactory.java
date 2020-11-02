package com.example.uploadimages.networkUtils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.networkUtils.repositories.CategoryRepo;
import com.example.uploadimages.networkUtils.viewmodel.CategoryViewModel;

public class CategoryFactory implements ViewModelProvider.Factory {
    CategoryRepo categoryRepo;

    public CategoryFactory(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(categoryRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
