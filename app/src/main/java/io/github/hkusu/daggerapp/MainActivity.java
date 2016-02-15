package io.github.hkusu.daggerapp;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.hkusu.daggerapp.adapter.TodoListAdapter;
import io.github.hkusu.daggerapp.di.AppComponent;
import io.github.hkusu.daggerapp.model.repository.TodoRepository;
import io.github.hkusu.daggerapp.service.RxEventBus;
import io.github.hkusu.daggerapp.viewcontroller.UserEventViewController;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {
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

    private UserEventViewController userEventViewController;
    private TodoRepository todoRepository;
    private RxEventBus rxEventBus;
    private TodoListAdapter todoListAdapter; // ListView用のAdapter
    private Subscription subscription; // イベント購読用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // ButterKnife

        // Dagger
        AppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        userEventViewController = appComponent.provideUserEventViewController();
        todoRepository = appComponent.provideTodoRepository();
        rxEventBus = appComponent.provideRxEventBus();

        // ToolBarの設定
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // ListAdapterを作成
        todoListAdapter = new TodoListAdapter(
                this,
                R.layout.adapter_todo_list,
                todoRepository.get() // ListViewに表示するデータセット
        );
        // ListViewにAdapterをセット
        todoListView.setAdapter(todoListAdapter);

        // 起動時にソフトウェアキーボードが表示されないようにする
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        userEventViewController.onCreate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userEventViewController.onStart(null);
        // 画面の初期表示
        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userEventViewController.onResume();
        // Todoデータの変更イベントを購読 *Viewの操作を伴う為メインスレッドで受ける*
        subscription = rxEventBus.onEventMainThread(TodoRepository.ChangedEvent.class, event -> {
            // 画面の表示を更新
            updateView();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        userEventViewController.onPause();
        subscription.unsubscribe(); // 購読を解除
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this); // ButterKnife
        userEventViewController.onDestroy();
    }

    // 画面の表示を更新するPrivateメソッド
    @MainThread
    private void updateView() {
        // データセットの変更があった旨をAdapterへ通知
        todoListAdapter.notifyDataSetChanged();
        // Todoデータの件数を更新
        countTextView.setText(String.valueOf(todoRepository.size()));
    }
}
