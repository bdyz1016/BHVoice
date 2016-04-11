package com.bhsc.net;

/**
 * Created by lynn on 10/30/15.
 */
public class ObjectResponse<T> extends Response{
    private int code;
    private T object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
