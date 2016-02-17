package io.github.hkusu.daggerapp.di;

import javax.inject.Singleton;

import dagger.Component;
import io.github.hkusu.daggerapp.model.repository.TodoRepository;
import io.github.hkusu.daggerapp.service.RxEventBus;
import io.github.hkusu.daggerapp.viewcontroller.UserEventViewController;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    // コンストラクタでインジェクトできないクラス(Activity等)でインスタンスの参照を取得する用
    UserEventViewController provideUserEventViewController();
    TodoRepository provideTodoRepository();
    RxEventBus provideRxEventBus();
}