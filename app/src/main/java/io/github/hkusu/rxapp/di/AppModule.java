package io.github.hkusu.rxapp.di;

import android.app.Application;

import com.github.gfx.android.orma.AccessThreadConstraint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.hkusu.rxapp.model.entity.OrmaDatabase;

@Singleton
@Module
public class AppModule {
    // コンストラクタでのインジェクトで生成方法を定義できないインスタンスはここで定義

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public OrmaDatabase provideOrmaDatabase() {
        return OrmaDatabase.builder(application)
                .readOnMainThread(AccessThreadConstraint.WARNING)
                .writeOnMainThread(AccessThreadConstraint.FATAL)
                .build();
    }
}



