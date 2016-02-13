package io.github.hkusu.daggerapp.viewcontroller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.github.hkusu.daggerapp.MainApplication;
import io.github.hkusu.daggerapp.R;
import io.github.hkusu.daggerapp.service.RxEventBus;
import io.github.hkusu.daggerapp.model.entity.Todo;
import io.github.hkusu.daggerapp.model.repository.TodoRepository;
import io.github.hkusu.daggerapp.adapter.TodoListAdapter;
import io.github.hkusu.daggerapp.viewcontroller.base.ButterKnifeViewController;
import rx.Subscription;

public class UserEventViewController extends ButterKnifeViewController<Void> {
    @Inject
    TodoRepository todoRepository;
    @Inject
    RxEventBus rxEventBus;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.todoEditText)
    EditText todoEditText;
    @Bind(R.id.createButton)
    Button createButton;
    @Bind(R.id.countTextView)
    TextView countTextView;
    @Bind(R.id.todoListView)
    ListView todoListView;

    private Subscription subscription; // イベント購読用

    @Inject
    public UserEventViewController() {
    }

    @Override
    public void onCreate(@NonNull Activity activity) {
        super.onCreate(activity);
        MainApplication.getAppComponent().inject(this); // Dagger
    }

    @Override
    public void onStart(@Nullable Void listener) {
        super.onStart(null);
        createButton.setEnabled(false); // 初期は[登録]ボタンを非活性に
    }

    @Override
    public void onResume() {
        // 削除ボタンの押下イベントを購読
        subscription = rxEventBus.onEvent(TodoListAdapter.ViewHolder.DeleteButtonClickedEvent.class, event -> {
            // データ削除
            todoRepository.delete(event.getId());
        });
    }

    @Override
    public void onPause() {
        subscription.unsubscribe(); // 購読を解除
    }

    // 入力エリアのテキスト変更
    @OnTextChanged(R.id.todoEditText)
    public void onTodoEditTextChanged() {
        // [登録]ボタンを活性化
        createButton.setEnabled(true);
    }

    // [登録]ボタン押下
    @OnClick(R.id.createButton)
    public void onCreateButtonClick() {
        // 入力内容が空の場合は何もしない
        if (todoEditText.getText().toString().equals("")) {
            return;
        }
        // Todoデータを登録
        registerTodo();
    }

    // 入力エリアでEnter
    @OnEditorAction(R.id.todoEditText)
    public boolean onTodoEditTextEditorAction(KeyEvent event) {
        // 入力内容が空の場合は何もしない
        if (todoEditText.getText().toString().equals("")) {
            return true;
        }
        // 前半はソフトウェアキーボードのEnterキーの判定、後半は物理キーボードでの判定
        if (event == null || (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
            // Todoデータを登録
            registerTodo();
        }
        return true;
    }

    // 画面での入力内容をRealmへ登録するPrivateメソッド
    @MainThread
    private void registerTodo() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        // Todoデータを作成
        Todo todo = new Todo();
        todo.setText(todoEditText.getText().toString());
        // データ登録
        if (!todoRepository.createOrUpdate(todo)) {
            return;
        }
        // 入力内容は空にする
        todoEditText.setText(null);
        // ソフトウェアキーボードを隠す
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(todoEditText.getWindowToken(), 0);
        // [登録]ボタンを非活性に
        createButton.setEnabled(false);
    }
}