package com.bhsc.mobile.userpages.event;

/**
 * Created by lynn on 10/28/15.
 */
public class UserEvent {
    public static final int ACTION_GET_USERINFO = 0x1;
    public static final int ACTION_UPDATE_USERINFO_SUCCESS = 0x2;
    public static final int ACTION_UPDATE_USERINFO_FAILED = 0x3;
    public static final int ACTION_DELETE_USERINFO_COMPLETE = 0x4;
    public static final int ACTION_LOGIN_SUCCESS = 0x5;
    public static final int ACTION_LOGIN_FAILED = 0x6;

    private int Action;
    private Object Extra;

    public int getAction() {
        return Action;
    }

    public void setAction(int action) {
        Action = action;
    }

    public Object getExtra() {
        return Extra;
    }

    public void setExtra(Object extra) {
        Extra = extra;
    }
}
