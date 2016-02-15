package io.github.hkusu.daggerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.hkusu.daggerapp.MainApplication;
import io.github.hkusu.daggerapp.R;
import io.github.hkusu.daggerapp.model.entity.Todo;
import io.github.hkusu.daggerapp.service.RxEventBus;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    private final LayoutInflater layoutInflater;
    private final int resource; // レイアウトXMLのid
    private static RxEventBus rxEventBus;

    public TodoListAdapter(Context context, int resource, List<Todo> objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        rxEventBus = ((MainApplication) context.getApplicationContext()).getAppComponent().provideRxBus();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        // 今回はViewHolderに状態を持つので毎回作成する
        viewHolder = new ViewHolder(convertView, rxEventBus);
        // この行のTodoデータを取得
        Todo todo = getItem(position);
        // Todoのテキストを表示
        viewHolder.todoTextView.setText(todo.getText());
        // [削除]ボタン用にidを保持
        viewHolder.id = todo.getId();

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.todoTextView)
        TextView todoTextView;
        @Bind(R.id.deleteButton)
        Button deleteButton;

        private RxEventBus rxEventBus;
        private int id; // Todoデータのid

        private ViewHolder(View view, RxEventBus rxEventBus) {
            ButterKnife.bind(this, view); // ButterKnife
            this.rxEventBus = rxEventBus;
        }

        // [削除]ボタン押下
        @OnClick(R.id.deleteButton)
        public void onDeleteButtonClick() {
            // ボタンが押下された旨を通知
            rxEventBus.post(new DeleteButtonClickedEvent(id));
        }

        //イベント通知用クラス
        public static class DeleteButtonClickedEvent {
            // Todoデータのid
            private int id;

            public DeleteButtonClickedEvent(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }
        }
    }
}