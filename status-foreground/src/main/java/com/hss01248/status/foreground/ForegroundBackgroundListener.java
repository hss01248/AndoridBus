package com.hss01248.status.foreground;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hss01248.appstartup.api.AppStartUpCallback;
import com.hss01248.bus.AndroidBus;
import com.hss01248.startup.annotation.AppStartUpItem;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 17:46
 * @Version 1.0
 */
@AppStartUpItem
public class ForegroundBackgroundListener  implements AppStartUpCallback, Application.ActivityLifecycleCallbacks {

   static int count = 0;

    static boolean isAppForeground(){
       return count>0;
   }
   //todo 启动过程中,怎么判断是前台启动还是后台启动?
    //todo 怎么过滤那些启动系统intent取数据然后回来的伪前后台切换?

    @Override
    public void onFirstProviderInit(Application app) {
       app.registerActivityLifecycleCallbacks(new ForegroundBackgroundListener());
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        count++;
        if(count == 1){
            AndroidBus.post(new AppForegroundBackgroundEvent(false));
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        count--;
        if(count == 0){
            AndroidBus.post(new AppForegroundBackgroundEvent(true));
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
