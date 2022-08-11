package com.hss01248.bus;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 14:07
 * @Version 1.0
 */
public interface BusObserver<T> extends DefaultLifecycleObserver {

    void observer(T obj);

    /**
     * 是否切换到ui线程. 默认false
     * @return
     */
    default boolean switchToUIThread(){
        return false;
    }

    @Override
    default void onDestroy(@NonNull LifecycleOwner owner) {
        AndroidBus.removeObserverMannually(this);
    }

    default void unSubscribeSelf(){
        AndroidBus.removeObserverMannually(this);
    }

    //静态方法,避免持有外部类引用, 但回调怎么办?
    public static <T> BusObserver<T> newIntance(Object... objects){
        return new BusObserver<T>() {
            @Override
            public void observer(T obj) {

            }
        };
    }
}
