package io.github.hkusu.daggerapp;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = PersistenceModule.class)
public interface PersistenceComponent {
    void inject(MainActivity activity);
}
