package com.bhsc.mobile.homepage.newsdetail;

/**
 * Created by lynn on 15-10-9.
 */
public class SupportEvent {

    public static final int STATE_POSITIVE = 1;
    public static final int STATE_NEGATIVE = 2;

    private int State;

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
