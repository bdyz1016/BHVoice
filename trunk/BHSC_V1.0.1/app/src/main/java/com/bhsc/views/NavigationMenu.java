package com.bhsc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bhsc.utils.L;

/**
 * Created by lynn on 15-9-16.
 */
public class NavigationMenu extends LinearLayout implements View.OnClickListener{
    private final String TAG = NavigationMenu.class.getSimpleName();

    public static interface OnMenuItemClickListener{
        void onItemClick(int id);
    }

    private OnMenuItemClickListener mOnMenuItemClickListener = new OnMenuItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }
    };

    private final int SIZE_UNSPECIFIED = -1;

    private final int DEFAULT_HEIGHT = 70;

    private final int DEFAULT_PADDING = 5;

    private int mMenuItemCount;

    private int mPadding;

    public NavigationMenu(Context context) {
        this(context, null);
    }

    public NavigationMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        L.i(TAG, "init");
        mPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING, context.getResources().getDisplayMetrics());
                setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0,mPadding,0,mPadding);
        this.post(new Runnable() {
            @Override
            public void run() {
                setItemAttrs();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if(widthMode == MeasureSpec.UNSPECIFIED){
            L.d("UNSPECIFIED");
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST);
        } else if(widthMode == MeasureSpec.AT_MOST){
            L.d("AT_MOST");
        } else if(widthMode == MeasureSpec.EXACTLY){
            L.d("EXACTLY");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getMeasuredHeight() + (int)mPadding);
    }


    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        mOnMenuItemClickListener.onItemClick(position);
    }

    private void setItemAttrs(){
        mMenuItemCount = getChildCount();
        L.i(TAG,"child count:" + mMenuItemCount);
        for(int i = 0;i < mMenuItemCount;i++){
            View item = getChildAt(i);
            LayoutParams params = (LayoutParams)item.getLayoutParams();
            params.weight = 1;
            params.width = LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER_VERTICAL;
            item.setLayoutParams(params);
            item.setOnClickListener(this);
            item.setTag(i);
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener){
        this.mOnMenuItemClickListener = listener;
    }

    public static int resolveSizeAndState(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
}
