package com.hss01248.status.foreground;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 17:51
 * @Version 1.0
 */
public class AppForegroundBackgroundEvent {

    public AppForegroundBackgroundEvent(boolean isToBackground) {
        this.isToBackground = isToBackground;
    }

    boolean isToBackground;

    @Override
    public String toString() {
        return "AppForegroundBackgroundEvent{" +
                "isToBackground=" + isToBackground +
                '}';
    }
}
