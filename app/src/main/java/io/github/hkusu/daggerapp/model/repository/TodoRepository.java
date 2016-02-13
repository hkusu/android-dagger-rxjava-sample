package io.github.hkusu.daggerapp.model.repository;

import java.util.List;

import javax.inject.Inject;

import io.github.hkusu.daggerapp.MainApplication;
import io.github.hkusu.daggerapp.service.RxEventBus;
import io.github.hkusu.daggerapp.model.entity.Todo;
import io.github.hkusu.daggerapp.service.RealmService;

public class TodoRepository {
    @Inject
    RealmService realmService;
    @Inject
    RxEventBus rxEventBus;

    public TodoRepository() {
        MainApplication.getAppComponent().inject(this); // Dagger
    }

    public List<Todo> get() {
        return this.realmService.selectTodoAll();
    }

    public Todo get(int id) {
        return this.realmService.selectTodoById(id);
    }

    public boolean createOrUpdate(Todo todo) {
        if (realmService.insertOrUpdateTodo(todo)) {
            // データが変更された旨を通知
            rxEventBus.post(new ChangedEvent());
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (realmService.deleteTodoById(id)) {
            // データが変更された旨を通知
            rxEventBus.post(new ChangedEvent());
            return true;
        }
        return false;
    }

    public int size() {
        return this.realmService.selectTodoCount();
    }

    // イベント通知用クラス
    public static class ChangedEvent {
        // 特に渡すデータは無し
        public ChangedEvent() {
        }
    }
}
