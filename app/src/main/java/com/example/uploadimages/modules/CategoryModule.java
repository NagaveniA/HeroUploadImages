package com.example.uploadimages.modules;

import com.example.uploadimages.networkUtils.factories.CategoryFactory;
import com.example.uploadimages.networkUtils.repositories.CategoryRepo;

import dagger.Module;
import dagger.Provides;
@Module
public class CategoryModule {
    @Provides
    CategoryRepo provideCategoryRepository() {
        return new CategoryRepo();
    }

    @Provides
    CategoryFactory provideCategoryFactory(CategoryRepo categoryRepo) {
        return new CategoryFactory(categoryRepo);
    }
}
