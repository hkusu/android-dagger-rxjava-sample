package io.github.hkusu.daggerapp;

import android.app.Application;

import io.github.hkusu.daggerapp.di.AppComponent;
import io.github.hkusu.daggerapp.di.AppModule;
import io.github.hkusu.daggerapp.di.DaggerAppComponent;

public class MainApplication extends Application {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //appComponent = DaggerAppComponent.create();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}