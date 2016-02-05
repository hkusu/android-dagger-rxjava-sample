package io.github.hkusu.daggerapp.service;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.github.hkusu.daggerapp.model.entity.TodoEntity;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmService {

    /** シングルトンインスタンス */
    private static final RealmService INSTANCE = new RealmService();
    /** Realmのインスタンス */
    private final Realm realm = Realm.getDefaultInstance();

    /**
     * シングルトンインスタンスを返す
     *
     * @return Todoデータ操作Modelのシングルトンインスタンス
     */
    public static RealmService getInstance() {
        return INSTANCE;
    }

    /**
     * コンストラクタ *外部からのインスタンス作成は禁止*
     */
    private RealmService() {}

    /**
     * Todoデータ全件を取得(降順)
     *
     * @return TodoデータのList *RealmResult型であるためgetし直さなくても変更内容は動的に反映されることに注意*
     */
    public List<TodoEntity> get() {
        return realm.allObjectsSorted(TodoEntity.class, TodoEntity.SORT_KEY, RealmResults.SORT_ORDER_DESCENDING);
    }

    /**
     * idをキーにTodoデータを取得 ※現状は未使用*
     *
     * @param id 検索対象のTodoデータのid
     * @return Todoデータ(1件)
     */
    public TodoEntity getById(int id) {
        return realm.where(TodoEntity.class)
                .equalTo(TodoEntity.PRIMARY_KEY, id)
                .findFirst();
    }

    /**
     * Todoデータを登録
     *
     * @param todoEntity 登録するTodoデータ
     * @return 成否
     */
    public boolean createOrUpdate(final TodoEntity todoEntity) {
        if (todoEntity.getId() == 0) {
            // 登録されているTodoデータの最大idを取得し、+1 したものをidとする(つまり連番)
            todoEntity.setId(getMaxId() + 1);
        }

        // トランザクション開始
        realm.beginTransaction();
        try {
            // idにプライマリキーを張ってあるため既に同一idのデータが存在していれば更新となる
            realm.copyToRealmOrUpdate(todoEntity);
            // コミット
            realm.commitTransaction();
            // データが変更された旨をEventBusで通知
            EventBus.getDefault().post(new ChangedEvent());
        } catch (Exception e) {
            // ロールバック
            realm.cancelTransaction();
            return false;
        }
        return true;
    }

    /**
     * idをキーにTodoデータを削除
     *
     * @param id 削除対象のTodoデータのid
     * @return 成否
     */
    public boolean removeById(final int id) {
        // トランザクション開始
        realm.beginTransaction();
        try {
            // idに一致するレコードを削除
            realm.where(TodoEntity.class).equalTo(TodoEntity.PRIMARY_KEY, id).findAll().clear();
            // コミット
            realm.commitTransaction();
            // データが変更された旨をEventBusで通知
            EventBus.getDefault().post(new ChangedEvent());
        } catch (Exception e) {
            // ロールバック
            realm.cancelTransaction();
            return false;
        }
        return true;
    }

    /**
     * Todoデータの件数を取得
     *
     * @return 件数
     */
    public int getSize() {
        return realm.allObjects(TodoEntity.class).size();
    }

    /**
     * 登録されているTodoデータの最大idを取得
     *
     * @return 最大id
     */
    private int getMaxId() {
        return realm.where(TodoEntity.class).findAll().max(TodoEntity.PRIMARY_KEY).intValue();
    }

    /**
     * EventBus用のイベントクラス
     */
    public static class ChangedEvent {
        // 特に渡すデータは無し
        private ChangedEvent() {
        }
    }

}