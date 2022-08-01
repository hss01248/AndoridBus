package com.hss01248.login;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/22 14:54
 * @Version 1.0
 */
public class LoginEvent2 {

    public LoginEvent2(Object userDetail) {
        this.userDetail = userDetail;
    }

    public Object userDetail;

    public LoginEvent2() {
    }

    @Override
    public String toString() {
        return "LoginEvent2{" +
                "userDetail=" + userDetail +
                '}';
    }
}
