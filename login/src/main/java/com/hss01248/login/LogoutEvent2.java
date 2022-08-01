package com.hss01248.login;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/22 14:55
 * @Version 1.0
 */
public class LogoutEvent2 {

    public boolean fromLoginPage;

    public LogoutEvent2 setFromLoginPage(boolean fromLoginPage) {
        this.fromLoginPage = fromLoginPage;
        return this;
    }

    public LogoutEvent2 setFromKickOutMsg(boolean fromKickOutMsg) {
        this.fromKickOutMsg = fromKickOutMsg;
        return this;
    }

    public boolean fromKickOutMsg;

    @Override
    public String toString() {
        return "LogoutEvent2{" +
                "fromLoginPage=" + fromLoginPage +
                ", fromKickOutMsg=" + fromKickOutMsg +
                '}';
    }
}
