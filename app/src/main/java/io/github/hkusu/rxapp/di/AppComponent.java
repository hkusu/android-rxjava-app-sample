package io.github.hkusu.rxapp.di;

import javax.inject.Singleton;

import dagger.Component;
import io.github.hkusu.rxapp.model.usecase.UserUseCase;
import io.github.hkusu.rxapp.lib.RxEventBus;
import io.github.hkusu.rxapp.lib.SubscriptionManager;
import io.github.hkusu.rxapp.ui.controller.UserEventController;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    // コンストラクタでインジェクトできないクラス(Activity等)でインスタンスの参照を取得する用
    UserEventController provideUserEventController();
    UserUseCase provideUserUseCase();
    RxEventBus provideRxEventBus();
    SubscriptionManager provideSubscriptionManager();
}