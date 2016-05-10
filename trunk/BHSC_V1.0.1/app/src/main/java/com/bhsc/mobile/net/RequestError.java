package com.bhsc.mobile.net;

/**
 * Created by zhanglei on 16/5/9.
 */
public class RequestError {
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
