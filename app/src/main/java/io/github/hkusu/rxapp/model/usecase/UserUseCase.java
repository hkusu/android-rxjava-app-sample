package io.github.hkusu.rxapp.model.usecase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.hkusu.rxapp.model.entity.Todo;
import io.github.hkusu.rxapp.model.repository.TodoRepository;
import rx.Observable;

@Singleton
public class UserUseCase {
    private final TodoRepository todoRepository;

    @Inject
    public UserUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Observable<List<Todo>> getTodo() {
        return Observable.create(subs -> {
            todoRepository.get()
                    .subscribe(aTodoList -> {
                        subs.onNext(aTodoList);
                        subs.onCompleted();
                    });
        });
    }

    public Observable<Void> registerTodo(Todo todo) {
        return Observable.create(subs -> {
            todoRepository.create(todo)
                    .subscribe(aVoid -> {
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }

    public Observable<Void> unregisterTodo(long id) {
        return Observable.create(subs -> {
            todoRepository.delete(id)
                    .subscribe(aVoid -> {
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }
}
