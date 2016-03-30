package io.github.hkusu.rxapp.model.repository;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.hkusu.rxapp.model.entity.OrmaDatabase;
import io.github.hkusu.rxapp.model.entity.Todo;
import io.github.hkusu.rxapp.util.Util;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class TodoRepository {
    private static final Subject<Long, Long> subject = new SerializedSubject<>(PublishSubject.create());
    private final OrmaDatabase orma;
    private final List<Todo> todoListCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public static Observable<Long> getObservable() {
        return subject;
    }

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
                        subject.onNext(id[0]);
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
                        subject.onNext(id);
                        subs.onNext(null);
                        subs.onCompleted();
                    });
        });
    }
}