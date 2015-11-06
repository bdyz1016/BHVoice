package com.bhsc.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by lynn on 11/2/15.
 */
public class NewsListView extends ListView {

    private View mFooterView;

    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height = getMeasuredHeight();
//        int childHeight = 0;
//        if(getCount() > 0){
//            final View child = getChildAt(0);
//            childHeight = getMeasuredHeight() + child.getMeasuredHeight() * getCount();
//        }
//        if(childHeight > height){
//            changeViewHeight(mFooterView, mFooterView.getMeasuredHeight());
//        } else {
//            changeViewHeight(mFooterView, 0);
//        }
    }

    private void changeViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (null == lp) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        }
        lp.height = height;
        view.setLayoutParams(lp);
    }

    @Override
    public void addFooterView(View v) {
        super.addFooterView(v);
//        this.mFooterView = v;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
