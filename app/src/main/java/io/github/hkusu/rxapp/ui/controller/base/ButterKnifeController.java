package io.github.hkusu.rxapp.ui.controller.base;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * @see <a href="http://qiita.com/hkusu/items/2e8fb40442190b440029">Controllerへ処理を委譲する</a>
 */
public abstract class ButterKnifeController<T> {
    private Activity activity;
    private T listener;

    @Nullable
    @CheckResult
    protected final Activity getActivity() {
        return activity;
    }

    @Nullable
    @CheckResult
    protected final T getListener() {
        return listener;
    }

    @CallSuper
    public void onCreate(@NonNull Activity activity) {
        ButterKnife.bind(this, activity);
        this.activity = activity;
    }

    @CallSuper
    public void onStart(@Nullable T listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public void onResume() {}

    public void onPause() {}

    public void onStop() {}

    @CallSuper
    public void onDestroy() {
        ButterKnife.unbind(this);
        activity = null;
        listener = null; // onStopで処理したいがそのためにコールさせるのは冗長なので
    }
}