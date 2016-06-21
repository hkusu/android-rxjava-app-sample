package io.github.hkusu.rxapp.lib;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SubscriptionManager {
    private final RxEventBus bus;
    private final CompositeSubscription cs;

    @Inject
    public SubscriptionManager (RxEventBus bus) {
        this.bus = bus;
        this.cs = new CompositeSubscription();
    }

    public <T> void subscribe(Class<T> clazz, Scheduler scheduler, Action1<T> action) {
        cs.add(bus.subscribe(clazz, scheduler, action));
    }

    public <T> void subscribePostThread(Class<T> clazz, Action1<T> action) {
        cs.add(bus.subscribePostThread(clazz, action));
    }

    public <T> void subscribeMainThread(Class<T> clazz, Action1<T> action) {
        cs.add(bus.subscribeMainThread(clazz, action));
    }

    public <T> void subscribeBackgroundThread(Class<T> clazz, Action1<T> action) {
        cs.add(bus.subscribeBackgroundThread(clazz, action));
    }

    public <T> void subscribe(Observable<T> observable, Scheduler scheduler, Action1<T> action) {
        cs.add(observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(scheduler)
                        .subscribe(action)
        );
    }

    public <T> void subscribeMainThread(Observable<T> observable, Action1<T> action) {
        subscribe(observable, AndroidSchedulers.mainThread(), action);
    }

    public <T> void subscribeBackgroundThread(Observable<T> observable, Action1<T> action) {
        subscribe(observable, Schedulers.io(), action);
    }

    public SubscriptionManager add(Subscription subscription) {
        cs.add(subscription);
        return this;
    }

    public void clear() {
        if (cs.hasSubscriptions()) {
            cs.clear();
        }
    }

    public void unsubscribe() {
        if (!cs.isUnsubscribed()) {
            cs.unsubscribe();
        }
    }
}