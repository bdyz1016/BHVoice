package com.bhsc.mobile.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lynn on 11/4/15.
 */
public class RefreshLayout extends SwipeRefreshLayout {

    private float mLastMotionX;
    private float mLastMotionY;

    public RefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
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
//                double angle = Math.abs(Math.atan(xDiff / yDiff));
//                if (angle > 0.35) {
//                    return false;
//                }
                if(xDiff * 4 > yDiff){
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
        return super.onTouchEvent(ev);
    }
}
