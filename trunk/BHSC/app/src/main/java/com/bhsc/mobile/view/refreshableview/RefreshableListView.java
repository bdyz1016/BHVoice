package com.bhsc.mobile.view.refreshableview;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bhsc.mobile.utils.L;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 12/15/14.
 */
public class RefreshableListView extends ListView {
    private static final String TAG = RefreshableListView.class.getSimpleName();

    public interface OnStateChangedListener {
        void onRefreshStateChanged(int refreshState);

        void onLoadStateChanged(int loadState);
    }

    public RefreshableListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 刷新状态
     */
    public static final int STATE_REFRESH_NORMAL = 0x21;
    public static final int STATE_REFRESH_NOT_ARRIVED = 0x22;
    public static final int STATE_REFRESH_ARRIVED = 0x23;
    public static final int STATE_REFRESHING = 0x24;
    private int refreshState;

    /**
     * 加载状态
     */
    public static final int STATE_LOAD_NORMAL = 0x31;
    public static final int STATE_LOAD_NOT_ARRIVED = 0x32;
    public static final int STATE_LOAD_ARRIVED = 0x33;
    public static final int STATE_LOADING = 0x34;
    private int loadState;

    private OnStateChangedListener mOnStateChangedListener = new OnStateChangedListener() {
        @Override
        public void onRefreshStateChanged(int refreshState) {

        }

        @Override
        public void onLoadStateChanged(int loadState) {

        }
    };
    /**
     * 刷新的view
     */
    private View refreshHeaderView;

    private View loadFooterView;

    /**
     * 刷新的view的真实高度
     */
    private int originRefreshHeight;
    /**
     * 有效下拉刷新需要达到的高度
     */
    private int refreshArrivedStateHeight;
    /**
     * 刷新时显示的高度
     */
    private int refreshingHeight;
    /**
     * 正常未刷新高度
     */
    private int refreshNormalHeight;

    /**
     * 刷新的view的真实高度
     */
    private int originLoadHeight;
    /**
     * 有效下拉刷新需要达到的高度
     */
    private int loadArrivedStateHeight;
    /**
     * 刷新时显示的高度
     */
    private int loadingHeight;
    /**
     * 正常未刷新高度
     */
    private int loadNormalHeight;

    private int mTouchSlop;


    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "[onSizeChanged]w: " + w + ", h: " + h);
        Log.d(TAG, "[onSizeChanged]oldw: " + oldw + ", oldh: " + oldh);
        Log.d(TAG, "[onSizeChanged]child counts: " + this.getChildCount());

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void addHeaderView(View v) {
        refreshHeaderView = v;
        // 计算refreshHeadView尺寸
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        refreshHeaderView.measure(width, expandSpec);

        originRefreshHeight = refreshHeaderView.getMeasuredHeight();

        refreshArrivedStateHeight = originRefreshHeight;
        refreshingHeight = originRefreshHeight;
        refreshNormalHeight = 0;
        Log.d(TAG, "[onSizeChanged]refreshHeaderView origin height: " + originRefreshHeight);
        changeViewHeight(refreshHeaderView, refreshNormalHeight);

        // 初始化为正常状态
        setRefreshState(STATE_REFRESH_NORMAL);
        super.addHeaderView(refreshHeaderView);
    }

    public void addFooterView(View v) {
        loadFooterView = v;
        // 计算refreshHeadView尺寸
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        loadFooterView.measure(width, expandSpec);

        originLoadHeight = loadFooterView.getMeasuredHeight();

        loadArrivedStateHeight = originLoadHeight;
        loadingHeight = originLoadHeight;
        loadNormalHeight = 0;

        changeViewHeight(loadFooterView, loadNormalHeight);

        // 初始化为正常状态
        setLoadState(STATE_LOAD_NORMAL);

        super.addFooterView(loadFooterView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    private float lastDownY = Float.MAX_VALUE;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "[onTouchEvent]ev action: " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                super.onTouchEvent(ev);
                lastDownY = ev.getY();
                Log.d(TAG, "Down --> lastDownY: " + lastDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                float curY = ev.getY();
                float deltaY = curY - lastDownY;
                Log.d(TAG, "Move --> deltaY(curY - downY): " + deltaY);

                if (Math.abs(deltaY) > mTouchSlop && isPullDown(deltaY)) {
                    int curHeight = refreshHeaderView.getMeasuredHeight();
                    int exceptHeight = curHeight + (int) (deltaY / 2);

                    if (refreshState == STATE_REFRESH_NORMAL) { // 正常状态，手指往上（列表往下滚动）
                        super.onTouchEvent(ev);
                    } else {
                        // 如果当前没有处在正在刷新状态，则更新刷新状态
                        if (STATE_REFRESHING != refreshState) {
                            if (curHeight >= refreshArrivedStateHeight) { // 达到可刷新状态
                                setRefreshState(STATE_REFRESH_ARRIVED);
                            } else if (curHeight == refreshNormalHeight) { // 正常状态
                                setRefreshState(STATE_REFRESH_NORMAL);
                            } else { // 未达到可刷新状态
                                setRefreshState(STATE_REFRESH_NOT_ARRIVED);
                            }
                            changeViewHeight(refreshHeaderView, Math.max(refreshNormalHeight, exceptHeight));
                        } else {
                            super.onTouchEvent(ev);
                        }

                    }

                } else if (Math.abs(deltaY) > mTouchSlop && isPullUp(deltaY)) {
                    int cutFooterHeight = loadFooterView.getMeasuredHeight();
                    int exceptFooterHeight = cutFooterHeight + (int) Math.abs(deltaY / 2);

                    if (loadState == STATE_LOAD_NORMAL) { // 正常状态，手指往上（列表往下滚动）
                        super.onTouchEvent(ev);
                    } else {
                        if (STATE_LOADING != loadState) {
                            if (cutFooterHeight >= loadArrivedStateHeight) {
                                setLoadState(STATE_LOAD_ARRIVED);
                            } else if (cutFooterHeight == loadNormalHeight) {
                                setLoadState(STATE_LOAD_NORMAL);
                            } else {
                                setLoadState(STATE_LOAD_NOT_ARRIVED);
                            }
                            changeViewHeight(loadFooterView, Math.max(loadNormalHeight, exceptFooterHeight));
                        }
                    }
                } else {
                    super.onTouchEvent(ev);
                }
                lastDownY = curY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(ev);
                lastDownY = Float.MAX_VALUE;
                Log.d(TAG, "Up --> lastDownY: " + lastDownY);
                // 如果是达到刷新状态，则设置正在刷新状态的高度
                if (STATE_REFRESH_ARRIVED == refreshState) { // 达到了刷新的状态
                    startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshingHeight);
                    setRefreshState(STATE_REFRESHING);
                } else if (STATE_REFRESHING == refreshState) { // 正在刷新的状态
                    startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshingHeight);
                } else {
                    // 执行动画后回归正常状态
                    startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshNormalHeight, normalAnimatorListener);
                }
                if (STATE_LOAD_ARRIVED == loadState) {
                    startHeightAnimation(loadFooterView, loadFooterView.getMeasuredHeight(), loadingHeight);
                    setLoadState(STATE_LOADING);
                } else if (STATE_LOADING == loadState) {
                    startHeightAnimation(loadFooterView, loadFooterView.getMeasuredHeight(), loadingHeight);
                } else {
                    startHeightAnimation(loadFooterView, loadFooterView.getMeasuredHeight(), loadNormalHeight, loadNormalAnimatorListener);
                }
                break;
        }
        return true;
    }


    /**
     * 刷新完毕后调用此方法
     */
    public void completeRefresh() {
        if (STATE_REFRESHING == refreshState) {
            setRefreshState(STATE_REFRESH_NORMAL);
            startHeightAnimation(refreshHeaderView, refreshHeaderView.getMeasuredHeight(), refreshNormalHeight);
        }
    }

    public void completeLoad(){
        if(STATE_LOADING == loadState){
            setLoadState(STATE_LOAD_NORMAL);
            startHeightAnimation(loadFooterView, loadFooterView.getMeasuredHeight(), loadNormalHeight);
        }
    }

    /**
     * 修改当前的刷新状态
     *
     * @param expectRefreshState
     */
    private void setRefreshState(int expectRefreshState) {
        if (expectRefreshState != refreshState) {
            refreshState = expectRefreshState;
            if (null != mOnStateChangedListener) {
                mOnStateChangedListener.onRefreshStateChanged(refreshState);
            }
        }
    }


    /**
     * 修改当前加载状态
     *
     * @param state
     */
    private void setLoadState(int state) {
        if (state != loadState) {
            loadState = state;
            if (null != mOnStateChangedListener) {
                mOnStateChangedListener.onRefreshStateChanged(loadState);
            }
        }
    }

    /**
     * 改变某控件的高度
     *
     * @param view
     * @param height
     */
    private void changeViewHeight(View view, int height) {
        Log.d(TAG, "[changeViewHeight]change Height: " + height);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (null == lp) {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        }
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 改变某控件的高度动画
     *
     * @param view
     * @param fromHeight
     * @param toHeight
     */
    private void startHeightAnimation(final View view, int fromHeight, int toHeight) {
        startHeightAnimation(view, fromHeight, toHeight, null);
    }

    private void startHeightAnimation(final View view, int fromHeight, int toHeight, Animator.AnimatorListener animatorListener) {
        if (toHeight == view.getMeasuredHeight()) {
            return;
        }
        ValueAnimator heightAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                L.i(TAG, "header高度:" + value);
                if (null == value) return;
                changeViewHeight(view, value);
            }
        });
        if (null != animatorListener) {
            heightAnimator.addListener(animatorListener);
        }
        heightAnimator.setInterpolator(new LinearInterpolator());
        heightAnimator.setDuration(300/*ms*/);
        heightAnimator.start();
    }

    AnimatorListenerAdapter normalAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            L.i(TAG, "动画结束");
            setRefreshState(STATE_REFRESH_NORMAL); // 回归正常状态
        }
    };

    AnimatorListenerAdapter loadNormalAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            L.i(TAG, "动画结束");
            setRefreshState(STATE_LOAD_NORMAL); // 回归正常状态
        }
    };

    public void setRefreshArrivedStateHeight(int refreshArrivedStateHeight) {
        this.refreshArrivedStateHeight = refreshArrivedStateHeight;
    }

    public void setRefreshingHeight(int refreshingHeight) {
        this.refreshingHeight = refreshingHeight;
    }

    public void setRefreshNormalHeight(int refreshNormalHeight) {
        this.refreshNormalHeight = refreshNormalHeight;
    }

    public int getOriginRefreshHeight() {
        return originRefreshHeight;
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.mOnStateChangedListener = listener;
    }


    private boolean isPullUp(float deltaY){
        if(deltaY > 0 || STATE_LOADING == loadState || null == loadFooterView){
            return false;
        }
        View lastChild = getChildAt(getCount() - 1);
        if(lastChild == null){
            return false;
        }
        if(lastChild.getBottom() <= getHeight() && getLastVisiblePosition() == getCount() - 2){
            return true;
        }
        return false;
    }

    private boolean isPullDown(float deltaY){
        if(deltaY < 0 || STATE_REFRESHING == refreshState || null == refreshHeaderView){
            return false;
        }
        View firstChild = getChildAt(0);
        if(firstChild.getTop() == 0){
            return true;
        }
        return false;
    }
}



