package com.hss01248.status.foreground;

import androidx.lifecycle.LifecycleOwner;

import com.hss01248.bus.AndroidBus;
import com.hss01248.bus.BusObserver;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 18:00
 * @Version 1.0
 */
public class AppForegroundBackgroundManager {


   public static boolean isAppForeground(){
        return ForegroundBackgroundListener.isAppForeground();
    }

    public static void observer( LifecycleOwner lifecycleOwner, BusObserver<AppForegroundBackgroundEvent> observer){
        AndroidBus.observer(lifecycleOwner,observer);
    }

}
