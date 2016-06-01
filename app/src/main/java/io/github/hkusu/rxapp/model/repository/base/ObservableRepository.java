package io.github.hkusu.rxapp.model.repository.base;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public abstract class ObservableRepository<T> {
    private final Subject<T, T> subject = new SerializedSubject<>(PublishSubject.create());

    @NonNull
    @CheckResult
    public final rx.Observable<T> getObservable() {
        return subject;
    }

    protected final void notify(@NonNull T t) {
        subject.onNext(t);
    }
}
