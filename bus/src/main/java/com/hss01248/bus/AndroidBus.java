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
    //static List<BusObserver> onceObservers = new CopyOnWriteArrayList<>();
    static Handler mainHandler;
    public static String TAG = "AndroidBus";
    public static boolean enableLog = true;

    public static <T> void post(T obj) {
        Class<?> aClass = obj.getClass();
        if (map.containsKey(aClass)) {
            //必须完全匹配,不能是子类.
            List<BusObserver> busObservers = map.get(aClass);
            if(busObservers != null && !busObservers.isEmpty()){
                dispatchObservers(busObservers,enableLog,obj);
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

     static <T> void dispatchObservers(List<BusObserver> busObservers, boolean enableLog, T obj) {
        if (busObservers != null && !busObservers.isEmpty()) {
            for (BusObserver busObserver : busObservers) {

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
        }
    }

    /**
     * 提供手动移除observer的api. 适用于多页面长流程结束的移除
     * @param observer
     * @param <T>
     */
    public static <T> void removeObserverMannually(BusObserver<T> observer) {
        try {
            for (Map.Entry<Class, List<BusObserver>> classListEntry : map.entrySet()) {
                List<BusObserver> value = classListEntry.getValue();
                if (value.contains(observer)) {
                    if(enableLog){
                        Log.d(TAG,"remove observer from list, "+observer);
                    }
                    value.remove(observer);
                }
            }
        } catch (Throwable throwable) {
            if (enableLog) {
                Log.w(TAG, throwable);
            }
        }

        try {
            for (Map.Entry<String, List<BusObserver>> classListEntry : _AndroidTagBus.map.entrySet()) {
                List<BusObserver> value = classListEntry.getValue();
                if (value.contains(observer)) {
                    if(enableLog){
                        Log.d(TAG,"remove observer from tag list, "+observer);
                    }
                    value.remove(observer);
                }
            }
        } catch (Throwable throwable) {
            if (enableLog) {
                Log.w(TAG, throwable);
            }
        }
    }

    public static  void removeObserverByTag(String tag) {
        try {
            _AndroidTagBus.map.remove(tag);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
    public static  void removeObserverByType(Class clazz) {
        try {
            AndroidBus.map.remove(clazz.getName());
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static <T> void observerByTag(String tag, @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer) {
        _AndroidTagBus.observerByTag(tag,lifecycleOwner,observer);
    }

    public static <T> void observerByTag(String tag, @NonNull ContextBusObserver<T> observer) {
        observerByTag(tag,observer.getLifecyclerFromObj(),observer);
    }

    public static <T> void observer( @NonNull ContextBusObserver<T> observer) {
        observer(observer.getLifecyclerFromObj(),observer);
    }
    public static <T> void observer( @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer) {
        if (enableLog) {
            Log.v(TAG, "prepare to add observer : once="  + ", lifecycle:" + lifecycleOwner + ", observer" + observer);
        }
        Type[] types = observer.getClass().getGenericInterfaces();
        //直接实现的接口时
        if (types == null || types.length == 0) {
            Type type0 = observer.getClass().getGenericSuperclass();
            //实现的是类时
            if (type0 == null ) {
                Log.w(TAG, "not impl of  BusObserver<T> 0");
                return;
            }else {
                types = new Type[]{type0};
                Log.i(TAG, "get types form super class");
            }
        }
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                //实现里,泛型都取第一个泛型
                Class aClass = GenericClassUtil.getClass(type, 0);
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
            } else {
                if (enableLog) {
                    Log.w(TAG, "not impl of  BusObserver<T>");
                }
            }
        }
    }
}
