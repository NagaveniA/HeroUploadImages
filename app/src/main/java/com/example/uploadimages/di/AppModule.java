package com.example.uploadimages.di;

import android.content.Context;

import com.example.uploadimages.App;
import com.example.uploadimages.sessionManager.LoginSessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codebele on 20-Oct-20.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideApplicationContent(App app) {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    LoginSessionManager providePreferenceManager(Context context) {
        return new LoginSessionManager(context);
    }
}
