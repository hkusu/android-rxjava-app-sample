package io.github.hkusu.rxapp.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class RxEventBus {
    private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());

    @Inject
    public RxEventBus() {}

    public <T> Subscription subscribe(Class<T> clazz, Scheduler scheduler, Action1<T> action) {
        return subject
                .ofType(clazz)
                .observeOn(scheduler)
                .subscribe(action);
    }

    public <T> Subscription subscribePostThread(Class<T> clazz, Action1<T> action) {
        return subject
                .ofType(clazz)
                .subscribe(action);
    }

    public <T> Subscription subscribeMainThread(Class<T> clazz, Action1<T> action) {
        return subscribe(clazz, AndroidSchedulers.mainThread(), action);
    }

    public <T> Subscription subscribeBackgroundThread(Class<T> clazz, Action1<T> action) {
        return subscribe(clazz, Schedulers.io(), action);
    }

    public void post(Object event) {
        subject.onNext(event);
    }
}