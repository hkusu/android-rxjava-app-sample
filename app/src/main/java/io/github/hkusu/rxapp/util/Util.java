package io.github.hkusu.rxapp.util;

import android.util.Log;

import io.github.hkusu.rxapp.BuildConfig;

public class Util {
    public static void logThread(String methodName) {
        if (BuildConfig.DEBUG) {
            Log.d("RxJava-thread" + methodName, Thread.currentThread().getName());
        }
    }
}
