package com.bhsc.mobile.net;

/**
 * Created by lynn on 10/30/15.
 */
public class ObjectResponse<T> extends Response{

    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
