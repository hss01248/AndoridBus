package com.hss01248.bus;

import android.util.Log;

import java.util.Arrays;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 18:35
 * @Version 1.0
 */
public abstract class NoOuterRefBusObserver<T> implements BusObserver<T> {
    public NoOuterRefBusObserver() {
        //this$0
        Log.w(AndroidBus.TAG,"this$0 2: "+ Arrays.toString(getClass().getDeclaredFields()));
    }


}
