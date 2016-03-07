package io.github.hkusu.rxapp.ui.adapter;

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
import io.github.hkusu.rxapp.MainApplication;
import io.github.hkusu.rxapp.R;
import io.github.hkusu.rxapp.model.entity.Todo;
import io.github.hkusu.rxapp.util.RxEventBus;

public class TodoListAdapter extends ArrayAdapter<Todo> {
    private final LayoutInflater layoutInflater;
    private final int resource; // レイアウトXMLのid
    private final RxEventBus bus;

    public TodoListAdapter(Context context, int resource, List<Todo> objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        bus = ((MainApplication) context.getApplicationContext()).getAppComponent().provideRxEventBus();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        // 今回はViewHolderに状態を持つので毎回作成する
        viewHolder = new ViewHolder(convertView, bus);
        // この行のTodoデータを取得
        Todo todo = getItem(position);
        // Todoのテキストを表示
        viewHolder.todoTextView.setText(todo.text);
        // [削除]ボタン用にidを保持
        viewHolder.id = todo.id;

        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.todoTextView)
        TextView todoTextView;
        @Bind(R.id.deleteButton)
        Button deleteButton;

        private RxEventBus bus;
        private int id; // Todoデータのid

        private ViewHolder(View view, RxEventBus bus) {
            ButterKnife.bind(this, view); // ButterKnife
            this.bus = bus;
        }

        // [削除]ボタン押下
        @OnClick(R.id.deleteButton)
        public void onDeleteButtonClick() {
            // ボタンが押下された旨を通知
            bus.post(new DeleteButtonClickedEvent(id));
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