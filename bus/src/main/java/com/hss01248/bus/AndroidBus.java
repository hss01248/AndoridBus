package com.hss01248.bus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 14:05
 * @Version 1.0
 */
public class AndroidBus {

    static Map<Class, List<BusObserver>> map = new ConcurrentHashMap<>();
    static List<BusObserver> onceObservers = new CopyOnWriteArrayList<>();
    static Handler mainHandler;
    public static String TAG = "AndroidBus";
    public static boolean enableLog = true;

    public static <T> void post(T obj) {
        Class<?> aClass = obj.getClass();
        if (map.containsKey(aClass)) {
            //必须完全匹配,不能是子类.
            List<BusObserver> busObservers = map.get(aClass);
            if(busObservers != null && !busObservers.isEmpty()){
                dispatchObservers(busObservers,onceObservers,enableLog,obj);
            }else {
                if(enableLog)
                Log.w(TAG, "no observer for :" + obj);
            }
        } else {
            if(enableLog)
            Log.w(TAG, "no observer for 2:" + obj);
        }
    }

   public static <T> void postByTag(String tag, T obj) {
        _AndroidTagBus.postByTag(tag,obj);
    }

     static <T> void dispatchObservers(List<BusObserver> busObservers, List<BusObserver> onceObservers, boolean enableLog, T obj) {
        List<BusObserver> onceObserversToRemove = new ArrayList<>();
        if (busObservers != null && !busObservers.isEmpty()) {
            for (BusObserver busObserver : busObservers) {
                if (onceObservers.contains(busObserver)) {
                    onceObserversToRemove.add(busObserver);
                }
                if (busObserver.switchToUIThread()) {
                    if (mainHandler == null) {
                        mainHandler = new Handler(Looper.getMainLooper());
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (enableLog) {
                                    Log.i(TAG, Thread.currentThread().getName() + " --> dispatch to busObserver on main" + busObserver + "  , data: " + obj);
                                }
                                busObserver.observer(obj);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    });
                } else {
                    try {
                        if (enableLog) {
                            Log.i(TAG, Thread.currentThread().getName() + " --> dispatch to busObserver " + busObserver + "  , data: " + obj);
                        }
                        busObserver.observer(obj);
                    } catch (Throwable throwable) {
                        if (enableLog) {
                            Log.w(TAG, throwable);
                        }
                    }
                }
            }
            //移除一次性的监听器
            if (!onceObserversToRemove.isEmpty()) {
                try {
                    for (BusObserver busObserver : onceObserversToRemove) {
                        busObservers.remove(busObserver);
                        onceObservers.remove(busObserver);
                    }
                } catch (Throwable throwable) {
                    if (enableLog) {
                        Log.w(TAG, throwable);
                    }
                }
            }
        }
    }

    public static <T> void removeObserverMannually(BusObserver<T> observer) {
        try {
            if (onceObservers.contains(observer)) {
                onceObservers.remove(observer);
            }
            for (Map.Entry<Class, List<BusObserver>> classListEntry : map.entrySet()) {
                List<BusObserver> value = classListEntry.getValue();
                if (value.contains(observer)) {
                    value.remove(observer);
                }
            }
        } catch (Throwable throwable) {
            if (enableLog) {
                Log.w(TAG, throwable);
            }
        }
    }

    public static <T> void observerByTag(String tag, boolean once, @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer) {
        _AndroidTagBus.observerByTag(tag,once,lifecycleOwner,observer);
    }
    public static <T> void observer(boolean once, @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer) {
        if (enableLog) {
            Log.v(TAG, "prepare to add observer : once=" + once + ", lifecycle:" + lifecycleOwner + ", observer" + observer);
        }

        Type[] types = observer.getClass().getGenericInterfaces();
        if (types == null || types.length == 0) {
            Log.w(TAG, "not impl of  BusObserver<T> 0");
            return;
        }
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                Class aClass = ClassUtil.getClass(type, 0);
                if (aClass == null) {
                    if (enableLog) {
                        Log.w(TAG, "ClassUtil.getClass is null : " + type);
                    }
                    continue;
                }
                if (enableLog) {
                    Log.d(TAG, "real add observer : " + observer + " , type:" + aClass);
                }
                List<BusObserver> busObservers = null;
                if (map.containsKey(aClass)) {
                    busObservers = map.get(aClass);
                } else {
                    busObservers = new ArrayList<>();
                    map.put(aClass, busObservers);
                }
                busObservers.add(observer);
                if (lifecycleOwner != null) {
                    lifecycleOwner.getLifecycle().addObserver(observer);
                }
                if (once) {
                    onceObservers.add(observer);
                }
            } else {
                if (enableLog) {
                    Log.w(TAG, "not impl of  BusObserver<T>");
                }
            }
        }

    }


}
