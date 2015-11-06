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
    public static final int ACTION_REGISTER_SUCCESS = 0x7;
    public static final int ACTION_REGISTER_FAILED = 0x8;


    public static final int ERROR_USERNAME_EMPTY = 0X1;
    public static final int ERROR_USERNAME_FORMAT = 0X2;
    public static final int ERROR_PASSWORD_EMPTY = 0X3;
    public static final int ERROR_PASSWORD_FORMAT = 0X4;
    public static final int ERROR_USERNAME_WRONG = 0X5;
    public static final int ERROR_PASSWORD_WRONG = 0X6;
    public static final int ERROR_PASSWORD_INCONSISTENT = 0X7;
    public static final int ERROR_USERNAME_EXIST = 0X8;
    public static final int ERROR_UNKNOWN = 0X20;

    private int Action;
    private int ErrorCode;
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

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
}
