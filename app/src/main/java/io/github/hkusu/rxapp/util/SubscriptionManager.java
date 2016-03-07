package io.github.hkusu.rxapp.util;

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
    private CompositeSubscription cs;

    @Inject
    public SubscriptionManager (RxEventBus bus) {
        this.bus = bus;
        this.cs = new CompositeSubscription();
    }

    public <T> void subscribe(Class<T> clazz, Scheduler scheduler, Action1<T> action) {
        getAddableCompositeSubscription().add(bus.subscribe(clazz, scheduler, action));
    }

    public <T> void subscribePostThread(Class<T> clazz, Action1<T> action) {
        getAddableCompositeSubscription().add(bus.subscribePostThread(clazz, action));
    }

    public <T> void subscribeMainThread(Class<T> clazz, Action1<T> action) {
        getAddableCompositeSubscription().add(bus.subscribeMainThread(clazz, action));
    }

    public <T> void subscribeBackgroundThread(Class<T> clazz, Action1<T> action) {
        getAddableCompositeSubscription().add(bus.subscribeBackgroundThread(clazz, action));
    }

    public <T> void subscribe(Observable<T> observable, Scheduler scheduler, Action1<T> action) {
        getAddableCompositeSubscription().add(observable
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
        getAddableCompositeSubscription().add(subscription);
        return this;
    }

    public void unsubscribe() {
        if (!cs.isUnsubscribed() && cs.hasSubscriptions()) {
            cs.unsubscribe();
        }
    }

    private CompositeSubscription getAddableCompositeSubscription() {
        if (cs.isUnsubscribed()) {
            return cs = new CompositeSubscription();
        }
        return cs;
    }
}