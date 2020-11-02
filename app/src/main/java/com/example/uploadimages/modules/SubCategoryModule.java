package com.example.uploadimages.modules;

import com.example.uploadimages.networkUtils.factories.SignInFactory;
import com.example.uploadimages.networkUtils.factories.SubCategoryFactory;
import com.example.uploadimages.networkUtils.repositories.SignInRepo;
import com.example.uploadimages.networkUtils.repositories.SubCategoryRepo;

import dagger.Module;
import dagger.Provides;
@Module
public class SubCategoryModule {

    @Provides
    SubCategoryRepo provideSubCategoryRepository() {
        return new SubCategoryRepo();
    }

    @Provides
    SubCategoryFactory provideSubCategoryFactory(SubCategoryRepo subCategoryRepo) {
        return new SubCategoryFactory(subCategoryRepo);
    }
}
