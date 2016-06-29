package com.bhsc.mobile.net;

/**
 * Created by zhanglei on 16/5/9.
 */
public class RequestError {

    public static final int ERROR_SUCCESS = 0;//请求成功
    public static final int ERROR_UNKNOWN = 1;//未知错误
    public static final int ERROR_SERVER_ISSUES = 2;//服务器错误
    public static final int ERROR_NETWORK_UNREACHABLE = 3;//网络不可达
    public static final int ERROR_TIME_OUT = 4;//网络不可达

    private String message;
    private int errorCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
