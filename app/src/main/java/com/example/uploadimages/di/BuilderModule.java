package com.example.uploadimages.di;

import com.example.uploadimages.activities.CategoryActivity;
import com.example.uploadimages.activities.LoginActivity;
import com.example.uploadimages.activities.SubCategoryActivity;
import com.example.uploadimages.activities.UploadImageActivity;
import com.example.uploadimages.modules.CategoryModule;
import com.example.uploadimages.modules.ImageUploadModule;
import com.example.uploadimages.modules.LoginModule;
import com.example.uploadimages.modules.SubCategoryModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Codebele on 20-Oct-20.
 * <p>
 * Binds all sub-components within the app.
 */

/**
 * Binds all sub-components within the app.
 */

@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = CategoryModule.class)
    abstract CategoryActivity bindCategoryActivity();

    @ContributesAndroidInjector(modules = SubCategoryModule.class)
    abstract SubCategoryActivity bindSubCategoryActivity();

    @ContributesAndroidInjector(modules = ImageUploadModule.class)
    abstract UploadImageActivity bindUploadImageActivity();
}
