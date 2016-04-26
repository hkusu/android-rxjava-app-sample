package io.github.hkusu.rxapp.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.hkusu.rxapp.lib.ObservableRepository;
import io.github.hkusu.rxapp.model.entity.OrmaDatabase;
import io.github.hkusu.rxapp.model.entity.Todo;
import rx.Observable;

@Singleton
public class TodoRepository extends ObservableRepository<Long> {
    private final OrmaDatabase orma;
    private final List<Todo> todoListCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    @Inject
    public TodoRepository(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Observable<Void> refresh() {
        return Observable.create(subs -> {
            Todo.relation(orma).selector().executeAsObservable().toList()
                    .doOnNext(aTodoList -> {
                        todoListCache.clear();
                        todoListCache.addAll(aTodoList);
                    })
                    .subscribe(aTodoList -> {
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }

    public Observable<List<Todo>> get() {
        return Observable.create(subs -> {
            if (!cacheLoaded) {
                refresh()
                        .doOnNext(aVoid -> cacheLoaded = true)
                        .subscribe(aVoid -> {
                            subs.onNext(todoListCache);
                            subs.onCompleted();
                        });
            } else {
                subs.onNext(todoListCache);
                subs.onCompleted();
            }
        });
    }

    public Observable<Void> create(Todo todo) {
        return Observable.create(subs -> {
            final long[] id = new long[1];
            Todo.relation(orma).inserter().executeAsObservable(todo)
                    .flatMapObservable(aLong -> {
                        id[0] = aLong;
                        return refresh();
                    })
                    .subscribe(aVoid -> {
                        notify(id[0]); // 変更通知
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }

    public Observable<Void> delete(long id) {
        return Observable.create(subs -> {
            Todo.relation(orma).deleter().idEq(id).executeAsObservable()
                    .flatMapObservable(aInteger -> refresh())
                    .subscribe(aVoid -> {
                        notify(id); // 変更通知
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }
}