package io.github.hkusu.rxapp.model.usecase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.hkusu.rxapp.model.entity.Todo;
import io.github.hkusu.rxapp.model.repository.TodoRepository;
import io.github.hkusu.rxapp.util.RxEventBus;
import rx.Observable;

@Singleton
public class UseCase {
    private final TodoRepository todoRepository;
    private final RxEventBus bus;

    @Inject
    public UseCase(TodoRepository todoRepository, RxEventBus bus) {
        this.todoRepository = todoRepository;
        this.bus = bus;
    }

    public Observable<List<Todo>> getTodo() {
        return todoRepository.get();
    }

    public Observable<Void> registerTodo(Todo todo) {
        return Observable.create(subs -> {
            todoRepository.create(todo)
                    .subscribe(aVoid -> {
                        bus.post(new TodoDataSetChangedEvent()); // データ変更通知
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }

    public Observable<Void> unregisterTodo(int id) {
        return Observable.create(subs -> {
            todoRepository.delete(id)
                    .subscribe(aVoid -> {
                        bus.post(new TodoDataSetChangedEvent()); // データ変更通知
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }

    // イベント通知用クラス
    public static class TodoDataSetChangedEvent {
    }
}
