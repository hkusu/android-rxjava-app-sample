package io.github.hkusu.rxapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.hkusu.rxapp.MainApplication;
import io.github.hkusu.rxapp.R;
import io.github.hkusu.rxapp.model.entity.Todo;
import io.github.hkusu.rxapp.model.usecase.UseCase;
import io.github.hkusu.rxapp.util.SubscriptionManager;
import io.github.hkusu.rxapp.util.Util;
import io.github.hkusu.rxapp.ui.controller.UserEventController;
import io.github.hkusu.rxapp.ui.adapter.TodoListAdapter;
import io.github.hkusu.rxapp.di.AppComponent;

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

    private UseCase useCase;
    private UserEventController userEventViewController;
    private SubscriptionManager subscriptionManager;
    private TodoListAdapter todoListAdapter; // ListView用のAdapter
    private final List<Todo> todoList = new ArrayList<>(); // ListView用のデータセット

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // ButterKnife

        // Dagger
        AppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        useCase = appComponent.provideUseCase();
        userEventViewController = appComponent.provideUserEventController();
        subscriptionManager = appComponent.provideSubscriptionManager();

        // ToolBarの設定
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // ListAdapterを作成
        todoListAdapter = new TodoListAdapter(
                this,
                R.layout.adapter_todo_list,
                todoList
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
        // 画面の表示を更新
        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userEventViewController.onResume();
        // Todoデータの変更イベントを購読
        subscriptionManager.subscribeMainThread(UseCase.TodoDataSetChangedEvent.class, event -> {
            // 画面の表示を更新
            updateView();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        userEventViewController.onPause();
        subscriptionManager.unsubscribe(); // 購読を解除
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
        subscriptionManager.subscribeMainThread(
                useCase.getTodo(), // 結果的にObservable型であればオペレータを繋ぐことも可能
                aTodoList -> {
                    // ListView のデータセットを変更
                    todoList.clear();
                    todoList.addAll(aTodoList);
                    // データセットの変更があった旨をAdapterへ通知
                    todoListAdapter.notifyDataSetChanged();
                    // Todoデータの件数表示を更新
                    countTextView.setText(String.valueOf(todoList.size()));
                });
    }
}
