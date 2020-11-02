package com.example.uploadimages.modules;

import com.example.uploadimages.networkUtils.factories.SignInFactory;
import com.example.uploadimages.networkUtils.repositories.SignInRepo;

import dagger.Module;
import dagger.Provides;
@Module
public class LoginModule {

    @Provides
    SignInRepo provideSignInRepository() {
        return new SignInRepo();
    }

    @Provides
    SignInFactory provideSignInFactory(SignInRepo signInRepo) {
        return new SignInFactory(signInRepo);
    }
}
