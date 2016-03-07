package io.github.hkusu.rxapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.github.hkusu.rxapp.di.AppComponent;
import io.github.hkusu.rxapp.di.AppModule;
import io.github.hkusu.rxapp.di.DaggerAppComponent;

public class MainApplication extends Application {
    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}