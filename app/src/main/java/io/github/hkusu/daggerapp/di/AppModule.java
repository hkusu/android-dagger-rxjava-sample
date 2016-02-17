package io.github.hkusu.daggerapp.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Singleton
@Module
public class AppModule {
    // コンストラクタでのインジェクトで生成方法を定義できないインスタンスはここで定義

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Realm provideRealm() {
        // Realm の初期設定
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(application).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        // 起動時に Realm のデータを削除(開発時)
        //if (BuildConfig.DEBUG) {
        //    Realm.deleteRealm(realmConfiguration);
        //}
        return Realm.getDefaultInstance();
    }
}



