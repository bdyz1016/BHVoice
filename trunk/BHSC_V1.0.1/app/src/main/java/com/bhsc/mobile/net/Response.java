package com.bhsc.mobile.net;

/**
 * Created by lynn on 11/2/15.
 */
public abstract class Response {
    public static int SUCESS_CODE = 200;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
