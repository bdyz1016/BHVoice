package com.bhsc.mobile.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.bhsc.mobile.R;

/**
 * Created by lynn on 10/30/15.
 */
public class BHRatioButton extends Button implements View.OnClickListener {

    public interface OnCheckedChangeListener{
        void onCheckedChanged(boolean isChecked);
    }

    public BHRatioButton(Context context) {
        this(context, null);
    }

    public BHRatioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private Drawable mNormal;
    private Drawable mChecked;

    private boolean isChecked = false;

    public BHRatioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioButton);

        mNormal = typedArray.getDrawable(R.styleable.RatioButton_normalDrawable);
        mChecked = typedArray.getDrawable(R.styleable.RatioButton_selectDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mNormal);
        } else {
            setBackgroundDrawable(mNormal);
        }

        setOnClickListener(this);

        typedArray.recycle();
    }

    @Override
    public void onClick(View v) {
        if(isChecked){
            isChecked = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mNormal);
            } else {
                setBackgroundDrawable(mNormal);
            }
        } else {
            isChecked = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mChecked);
            } else {
                setBackgroundDrawable(mChecked);
            }
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
        if(isChecked){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mChecked);
            } else {
                setBackgroundDrawable(mChecked);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mNormal);
            } else {
                setBackgroundDrawable(mNormal);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.mOnCheckedChangeListener = checkedChangeListener;
    }
}
