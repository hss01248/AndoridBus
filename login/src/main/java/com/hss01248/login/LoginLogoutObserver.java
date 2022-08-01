package com.hss01248.login;


import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.hss01248.bus.AndroidBus;
import com.hss01248.bus.BusObserver;

public interface LoginLogoutObserver<T>  {

    void login(T userDetail);

    void logout(boolean isFromLoginPage);


    public static <T> void observer(boolean once, @Nullable LifecycleOwner lifecycleOwner, LoginLogoutObserver<T> observer){
        AndroidBus.observer(once, lifecycleOwner, new BusObserver<LoginLogOutEvent>() {
            @Override
            public void observer(LoginLogOutEvent obj) {
                if(obj.loginSuccess){
                    observer.login((T) obj.userDetail);
                }else {
                    observer.logout(obj.fromLoginPage);
                }
            }
        });
    }
}
