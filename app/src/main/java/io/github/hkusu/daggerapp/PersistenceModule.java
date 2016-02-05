package io.github.hkusu.daggerapp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PersistenceModule {

    @Provides @Singleton
    public SomeManager provideSomeManager() {
        return new HogeSomeManager();
    }
}
