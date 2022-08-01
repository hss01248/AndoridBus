package com.hss01248.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.hss01248.bus.AndroidBus;
import com.hss01248.bus.BusObserver;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/22 14:54
 * @Version 1.0
 */
public class LoginLogOutEvent {



    public static LoginLogOutEvent successLogin(Object userDetail){
        LoginLogOutEvent event2 = new LoginLogOutEvent();
        event2.loginSuccess = true;
        event2.userDetail = userDetail;
        event2.fromLoginPage = true;
        return event2;
    }

    public static LoginLogOutEvent successLogout(){
        LoginLogOutEvent event2 = new LoginLogOutEvent();
        event2.loginSuccess = false;
        event2.fromLoginPage = false;
        return event2;
    }

    public static LoginLogOutEvent fromCancelLogin(){
        LoginLogOutEvent event2 = new LoginLogOutEvent();
        event2.loginSuccess = false;
        event2.fromLoginPage = true;
        return event2;
    }



    public Object userDetail;
    /**
     * true表示登录成功 false表示变成退出登录的状态
     */
    public boolean loginSuccess;

    /**
     * true表示从登录界面来
     * false表示点击logout按钮
     */
    public boolean fromLoginPage;



    private LoginLogOutEvent() {
    }

    @Override
    public String toString() {
        return "LoginLogOutEvent{" +
                "userDetail=" + userDetail +
                ", loginSuccess=" + loginSuccess +
                ", fromLoginPage=" + fromLoginPage +
                '}';
    }
}
