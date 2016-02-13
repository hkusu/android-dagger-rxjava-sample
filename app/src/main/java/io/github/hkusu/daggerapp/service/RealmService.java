package io.github.hkusu.daggerapp.service;

import java.util.List;

import javax.inject.Inject;

import io.github.hkusu.daggerapp.MainApplication;
import io.github.hkusu.daggerapp.model.entity.Todo;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmService {
    @Inject
    Realm realm;

    public RealmService() {
        MainApplication.getAppComponent().inject(this); // Dagger
    }

    // Todoデータ全件を取得(降順)
    // *RealmResult型であるためgetし直さなくても変更内容は動的に反映されることに注意*
    public List<Todo> selectTodoAll() {
        return realm.allObjectsSorted(Todo.class, Todo.SORT_KEY, RealmResults.SORT_ORDER_DESCENDING);
    }

    // idをキーにTodoデータを取得 ※現状は未使用*
    public Todo selectTodoById(int id) {
        return realm.where(Todo.class).equalTo(Todo.PRIMARY_KEY, id).findFirst();
    }

    // Todoデータを登録
    public boolean insertOrUpdateTodo(Todo todo) {
        synchronized (this) {
            if (todo.getId() == 0) {
                // 登録されているTodoデータの最大idを取得し、+1 したものをidとする(つまり連番)
                todo.setId(selectTodoMaxId() + 1);
            }
            // トランザクション開始
            realm.beginTransaction();
            try {
                // idにプライマリキーを張ってあるため既に同一idのデータが存在していれば更新となる
                realm.copyToRealmOrUpdate(todo);
                // コミット
                realm.commitTransaction();
            } catch (Exception e) {
                // ロールバック
                realm.cancelTransaction();
                return false;
            }
            return true;
        }
    }

    // idをキーにTodoデータを削除
    public boolean deleteTodoById(int id) {
        // トランザクション開始
        realm.beginTransaction();
        try {
            // idに一致するレコードを削除
            realm.where(Todo.class).equalTo(Todo.PRIMARY_KEY, id).findAll().clear();
            // コミット
            realm.commitTransaction();
        } catch (Exception e) {
            // ロールバック
            realm.cancelTransaction();
            return false;
        }
        return true;
    }

    // Todoデータの件数を取得
    public int selectTodoCount() {
        return realm.allObjects(Todo.class).size();
    }

    // 登録されているTodoデータの最大idを取得
    private int selectTodoMaxId() {
        return realm.where(Todo.class).findAll().max(Todo.PRIMARY_KEY).intValue();
    }
}