package com.hss01248.bus;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 19:03
 * @Version 1.0
 */
public abstract class ContextBusObserver<T> implements BusObserver<T> {

    public ContextBusObserver(Object contextOrFragment) {
        this.contextOrFragment = contextOrFragment;
    }

    public Object contextOrFragment;


    @Override
    public void observer(T obj) {
        Object obj2 = getLifecycledObjFromObj();
        if(obj2 instanceof Activity){
            Activity activity = (Activity) obj2;
            if(activity.isFinishing() || activity.isDestroyed()){
                AndroidBus.removeObserverMannually(this);
                return;
            }
        }else if(obj2 instanceof Fragment){
            Fragment fragment = (Fragment) obj2;
            if(fragment.isDetached() || fragment.isRemoving()){
                AndroidBus.removeObserverMannually(this);
                return;
            }
        }
        doObserverReally(obj);
    }

    protected abstract void doObserverReally(T obj);

    public LifecycleOwner getLifecyclerFromObj(){
        if(contextOrFragment instanceof LifecycleOwner){
            LifecycleOwner owner = (LifecycleOwner) contextOrFragment;
            return owner;
        }
        if(contextOrFragment instanceof Context){
            Activity activity = getActivityFromContext((Context) contextOrFragment);
            if(activity instanceof LifecycleOwner){
                return (LifecycleOwner)activity;
            }
            return null;
        }
        return null;
    }
    public Object getLifecycledObjFromObj(){
        if(contextOrFragment instanceof LifecycleOwner){
            LifecycleOwner owner = (LifecycleOwner) contextOrFragment;
            return owner;
        }
        if(contextOrFragment instanceof Context){
            Activity activity = getActivityFromContext((Context) contextOrFragment);
            return activity;
        }
        if(contextOrFragment instanceof Fragment){
            return contextOrFragment;
        }
        if(contextOrFragment instanceof android.app.Fragment){
            return contextOrFragment;
        }
        return null;
    }



    public static Activity getActivityFromContext(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
