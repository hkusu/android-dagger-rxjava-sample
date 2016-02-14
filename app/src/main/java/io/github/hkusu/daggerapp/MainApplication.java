package io.github.hkusu.daggerapp;

import android.app.Application;

import io.github.hkusu.daggerapp.di.AppComponent;
import io.github.hkusu.daggerapp.di.AppModule;
import io.github.hkusu.daggerapp.di.DaggerAppComponent;

public class MainApplication extends Application {
    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}