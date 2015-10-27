package com.bhsc.mobile.main.event;

/**
 * Created by lynn on 10/20/15.
 */
public class MainFrameEvent {
    public static final int ACTION_LOGINED = 0X1;
    public static final int ACTION_LOGOUT = 0X2;

    private int Action;

    public MainFrameEvent(int action){
        this.Action = action;
    }

    public int getAction() {
        return Action;
    }

    public void setAction(int action) {
        Action = action;
    }
}
