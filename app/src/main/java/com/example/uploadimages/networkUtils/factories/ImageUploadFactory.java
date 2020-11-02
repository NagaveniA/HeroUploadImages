package com.example.uploadimages.networkUtils.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.uploadimages.networkUtils.repositories.ImageUploadRepo;
import com.example.uploadimages.networkUtils.viewmodel.ImageUploadViewModel;

public class ImageUploadFactory implements ViewModelProvider.Factory {
    ImageUploadRepo imageUploadRepo;

    public ImageUploadFactory(ImageUploadRepo imageUploadRepo) {
        this.imageUploadRepo = imageUploadRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImageUploadViewModel.class)) {
            return (T) new ImageUploadViewModel(imageUploadRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
