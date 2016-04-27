package com.bhsc.mobile.news.newsdetail;

/**
 * Created by lynn on 11/5/15.
 */
public class DiscussEvent {
    public static final int ACTION_PUBLISH_SUCCESS = 0X1;
    public static final int ACTION_PUBLISH_FAILED = 0X2;
    public static final int ACTION_GET_ALL_DISCUSS = 0X3;

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
