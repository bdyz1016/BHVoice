package com.bhsc.mobile.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lynn on 11/3/15.
 */
public class MyViewPager extends ViewPager {

    private float mLastMotionX;
    private float mLastMotionY;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action != MotionEvent.ACTION_DOWN) {
            mLastMotionX = ev.getX();
            mLastMotionY = ev.getY();
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = x - mLastMotionX;
                final float dy = y - mLastMotionY;
                final float xDiff = Math.abs(dx);
                final float yDiff = Math.abs(dy);
                mLastMotionX = x;
                mLastMotionY = y;
                double angle = Math.abs(Math.atan(yDiff / xDiff));
                if (angle > Math.PI || angle < 1) {
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastMotionX = 0;
                mLastMotionY = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
