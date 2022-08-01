package com.hss01248.bus;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 16:43
 * @Version 1.0
 */
class _AndroidTagBus {

    static Map<String, List<BusObserver>> map = new ConcurrentHashMap<>();
    static List<BusObserver> onceObservers = new CopyOnWriteArrayList<>();

     static <T> void postByTag(String tag, T obj) {
        if (TextUtils.isEmpty(tag)) {
            if (AndroidBus.enableLog) {
                Log.w(AndroidBus.TAG, "tag is empty");
            }
            return;
        }
        if (map.containsKey(tag)) {
            List<BusObserver> busObservers = map.get(tag);
            if (busObservers != null && !busObservers.isEmpty()) {
                AndroidBus.dispatchObservers(busObservers, onceObservers, AndroidBus.enableLog, obj);
            } else {
                if (AndroidBus.enableLog) {
                    Log.w(AndroidBus.TAG, "not observer found on this tag: " + tag + ", obj: " + obj);
                }
            }
        } else {
            if (AndroidBus.enableLog) {
                Log.w(AndroidBus.TAG, "not observer found on this tag: " + tag + ", obj: " + obj);
            }
        }

    }

     static <T> void observerByTag(String tag, boolean once, @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer) {
        if (TextUtils.isEmpty(tag)) {
            Log.w(AndroidBus.TAG, "tag is empty " + observer);
            return;
        }
        if (AndroidBus.enableLog) {
            Log.d(AndroidBus.TAG, "real add observer by tag: "+tag+", once=" + once + ", lifecycle:" + lifecycleOwner + ", observer" + observer);
        }
        List<BusObserver> observers = null;
        if (map.containsKey(tag)) {
            observers = map.get(tag);
        }
        if (observers == null) {
            observers = new ArrayList<>();
            map.put(tag, observers);
        }
        observers.add(observer);
        if (once) {
            onceObservers.add(observer);
        }
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(observer);
        }
    }

}
