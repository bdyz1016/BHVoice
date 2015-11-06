package com.bhsc.mobile.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.L;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by lynn on 11/5/15.
 */
public class SwitchButton extends RelativeLayout {
    private final String TAG = SwitchButton.class.getSimpleName();

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnSwitchChangeListener{
        void onSwitchChanged(boolean arg);
    }

    private OnSwitchChangeListener mOnSwitchChangeListener = new OnSwitchChangeListener() {
        @Override
        public void onSwitchChanged(boolean arg) {

        }
    };

    private int mWidth = 180;
    private int mHeight = 120;
    private int mCentre = 60;

    private int mButtonSize = 120;

    private Drawable mSwitchOnDrawable;
    private Drawable mSwitchOffDrawable;
    private Drawable mSwitchButton;

    private float mLastDownX;
    private float mLastMotionX;

    private int mTouchSlop;

    private Button mButton;

    private float mStartPosition = 0;
    private float mEndPosition = mWidth - mButtonSize;
    private float mButtonLocation;

    private GestureDetector mGestureDetector;

    private boolean mSwitchOn = false;

    private void init(Context context) {

        mTouchSlop = 4;

        mSwitchOnDrawable = context.getResources().getDrawable(R.mipmap.on_switch_all);
        mSwitchOffDrawable = context.getResources().getDrawable(R.mipmap.off_switch_all);
        mSwitchButton = context.getResources().getDrawable(R.mipmap.button_switch_all);

        mButton = new Button(context);
        RelativeLayout.LayoutParams params = new LayoutParams(mButtonSize, mButtonSize);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(mButton, params);
        setBackgroundDrawable(mSwitchOnDrawable);
        mButton.setBackgroundDrawable(mSwitchButton);

        mButton.setFocusable(false);
        mButton.setClickable(false);
        setOnTouchListener(mTouchListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        } else if(widthMode == MeasureSpec.EXACTLY){
            mWidth = width;
            mCentre = width / 2;
        }
        if(heightMode == MeasureSpec.UNSPECIFIED){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else if(heightMode == MeasureSpec.EXACTLY){
            mHeight = height;
            mButtonSize = mHeight;
            mEndPosition = mWidth - mButtonSize;
            LayoutParams params = (LayoutParams) mButton.getLayoutParams();
            params.height = mButtonSize;
            params.width = mButtonSize;
            mButton.setLayoutParams(params);
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        if (widthMode != MeasureSpec.UNSPECIFIED) {
//            mWidth = widthSize;
//        }
//        if (heightMode != MeasureSpec.UNSPECIFIED) {
//            mHeight = heightSize;
//        }
//        LayoutParams params = (LayoutParams) mButton.getLayoutParams();
//        params.width = mHeight;
//        params.height = mHeight;
//        mButton.setLayoutParams(params);
//        mButton.measure(MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
//        setMeasuredDimension(mWidth, mHeight);
    }

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    L.i(TAG, "touch down");
                    mLastMotionX = event.getX();
                    mLastDownX = mLastMotionX;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    L.i("zhanglei", "touch move");
                    final float x = event.getX();
                    final float xDiff = x - mLastMotionX;
                    L.i(TAG, "x:" + x + ",LastX:" + mLastMotionX + ",xDiff:" + xDiff);
//                    L.i("zhanglei", "mTouchSlop:" + mTouchSlop);
                    mLastMotionX = x;
                    if (Math.abs(xDiff) < mTouchSlop) {
                        return false;
                    }
                    moveButton(xDiff);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(Math.abs(mLastMotionX - mLastDownX) < mTouchSlop){

                    }
                    mLastDownX = 0;
                    mLastMotionX = 0;
                    releaseButton();
                    break;
            }
            return true;
        }
    };

    private void moveButton(float xDiff) {
        L.i(TAG, "moveButton:" + xDiff);
        L.i("zhanglei", "End:" + mEndPosition);
        float distance = mButtonLocation + xDiff;
        L.i(TAG, "distance:" + distance);

        if(distance < 0){
            mButtonLocation = 0;
        } else if(distance > mEndPosition){
            mButtonLocation = mEndPosition;
        } else {
            mButtonLocation = distance;
        }
        LayoutParams params = (LayoutParams) mButton.getLayoutParams();
        params.leftMargin = (int) mButtonLocation;
        mButton.setLayoutParams(params);
    }

    private void releaseButton() {
        if (mButtonLocation + mButton.getWidth() / 2 > mCentre) {
            startAnimation((int) mButtonLocation, (int) mEndPosition);
        } else {
            startAnimation((int) mButtonLocation, 0);
        }
    }

    private void startAnimation(int from, int to) {
        ValueAnimator heightAnimator = ValueAnimator.ofInt(from, to);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                if (null == value) return;

                mButtonLocation = value;
                LayoutParams params = (LayoutParams) mButton.getLayoutParams();
                params.leftMargin = (int) mButtonLocation;
                mButton.setLayoutParams(params);
            }
        });
        heightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                L.i(TAG, "animation end");
                if(mButtonLocation == mStartPosition){
                    setSwitchOn();
                } else if(mButtonLocation == mEndPosition){
                    setSwitchOff();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if(mButtonLocation == mStartPosition){
                    setSwitchOn();
                } else if(mButtonLocation == mEndPosition){
                    setSwitchOff();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        heightAnimator.setInterpolator(new LinearInterpolator());
        heightAnimator.setDuration(100/*ms*/);
        heightAnimator.start();
    }

    public void setSwitchOn(){
        mSwitchOn = true;
        setBackgroundDrawable(mSwitchOnDrawable);
        mOnSwitchChangeListener.onSwitchChanged(mSwitchOn);
    }

    public void setSwitchOff(){
        mSwitchOn = false;
        setBackgroundDrawable(mSwitchOffDrawable);
        mOnSwitchChangeListener.onSwitchChanged(mSwitchOn);
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener listener){
        this.mOnSwitchChangeListener = listener;
    }
}
