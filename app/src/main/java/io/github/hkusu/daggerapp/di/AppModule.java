package io.github.hkusu.daggerapp.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.hkusu.daggerapp.service.RxEventBus;
import io.github.hkusu.daggerapp.model.repository.TodoRepository;
import io.github.hkusu.daggerapp.service.RealmService;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class AppModule {
    public AppModule(Application application) {
        // Realm の初期設定
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(application).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        // 起動時に Realm のデータを削除(開発時)
        //if (BuildConfig.DEBUG) {
        //    Realm.deleteRealm(realmConfiguration);
        //}
    }

    @Provides
    @Singleton
    public TodoRepository provideTodoRepository() {
        return new TodoRepository();
    }

    @Provides
    @Singleton
    public RealmService provideRealmService() {
        return new RealmService();
    }

    @Provides
    @Singleton
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public RxEventBus provideRxBus() {
        return new RxEventBus();
    }
}



