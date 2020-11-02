package com.example.uploadimages.modules;

import com.example.uploadimages.networkUtils.factories.ImageUploadFactory;
import com.example.uploadimages.networkUtils.repositories.ImageUploadRepo;

import dagger.Module;
import dagger.Provides;
@Module
public class ImageUploadModule {

    @Provides
    ImageUploadRepo provideImageUploadRepository() {
        return new ImageUploadRepo();
    }

    @Provides
    ImageUploadFactory provideImageUploadFactory(ImageUploadRepo imageUploadRepo) {
        return new ImageUploadFactory(imageUploadRepo);
    }
}
