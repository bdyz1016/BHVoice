//package com.bhsc.disclose;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.bhsc.mobile.R;
//
//import java.lang.ref.WeakReference;
//import java.util.LinkedList;
//
///**
// * Created by lynn on 11/2/15.
// */
//public class DiscloseListActivity extends Activity {
//    private final String TAG = DiscloseListActivity.class.getSimpleName();
//
//    class Views{
//        MyListView activity_disclose_list;
//        @InjectBinder(method = "createDisclose", listeners = {OnClick.class})
//        TextView activity_disclose_list_create;
//        ScrollView activity_discuss_scrollview;
//        @InjectBinder(method = "goBack", listeners = {OnClick.class})
//        View activity_discuss_back;
//        View activity_disclose_list_container;
//    }
//    @InjectAll
//    private Views mViews;
//    private LayoutInflater mInflater;
//
//    private SyncArrayList<Data_DB_Disclose> mDataList = new SyncArrayList<>(new LinkedList<Data_DB_Disclose>());
//
//    private AllDiscloseAdapter mAdapter;
//
//    private DisclosePresenter mDisclosePresenter;
//
//    private MyHandler mMyHandler;
//
//    private boolean mDiscloseButtonIsShow = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @InjectInit
//    private void init(){
//        initData();
//        initView();
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(mDiscloseButtonIsShow) {
//            hideCreateDiscloseButton();
//            mMyHandler.removeMessages(MyHandler.MEG_WHATE_RAISE_BTN);
//            mMyHandler.sendEmptyMessageDelayed(MyHandler.MEG_WHATE_RAISE_BTN, 2500);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    private void goBack(){
//        finish();
//    }
//
//    private void initData(){
//        mDisclosePresenter = DisclosePresenter.getInstance(this);
//        mMyHandler = new MyHandler(this);
//
//        mAdapter = new AllDiscloseAdapter(this, mDataList, new AllDiscloseAdapter.OnElementClickListener() {
//            @Override
//            public void onMenu(final int position) {
//                L.i(TAG, "onMenu");
//            }
//
//            @Override
//            public void onShare(int position) {
//                Intent intent = new Intent();
//                intent.setClass(DiscloseListActivity.this, ShareActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onSupport(int position) {
//                mDisclosePresenter.support(mDataList.get(position).getId());
//            }
//
//            @Override
//            public void onComment(int position) {
//
//            }
//        });
//
//
//        mInflater = LayoutInflater.from(this);
//    }
//
//    private void initView(){
//        mViews.activity_disclose_list.setAdapter(mAdapter);
//        mViews.activity_disclose_list.setFocusable(false);
//
//        mDisclosePresenter.getAllDisclose();
//
//        mMyHandler.sendEmptyMessageDelayed(MyHandler.MEG_WHATE_RAISE_BTN, 1000);
//    }
//
//    private void createDisclose(){
//        if(UserManager.getInstance(this).isLogined()) {
//            Intent intent = new Intent();
//            intent.setClass(this, DiscloseActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent();
//            intent.setClass(this, LoginAndRegisterActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    public void raiseCreateDiscloseButton(){
//        mDiscloseButtonIsShow = true;
//        try {
//            mViews.activity_disclose_list_create.clearAnimation();
//            Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
//            bounce.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    mViews.activity_disclose_list_create.clearAnimation();
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViews.activity_disclose_list_create.getLayoutParams();
//                    params.bottomMargin = 0;
//                    mViews.activity_disclose_list_create.setLayoutParams(params);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            mViews.activity_disclose_list_create.startAnimation(bounce);
//        } catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void hideCreateDiscloseButton(){
//        mDiscloseButtonIsShow = false;
//        mViews.activity_disclose_list_create.clearAnimation();
//        Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide);
//        bounce.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mViews.activity_disclose_list_create.clearAnimation();
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViews.activity_disclose_list_create.getLayoutParams();
//                params.bottomMargin = -mViews.activity_disclose_list_create.getMeasuredHeight();
//                mViews.activity_disclose_list_create.setLayoutParams(params);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mViews.activity_disclose_list_create.startAnimation(bounce);
//    }
//
//    private static class MyHandler extends Handler{
//        public static final int MEG_WHATE_RAISE_BTN = 0x1;
//        public static final int MEG_WHATE_HIDE_BTN = 0x2;
//
//        private final WeakReference<DiscloseListActivity> mWrActivity;
//        public MyHandler(DiscloseListActivity activity){
//            mWrActivity = new WeakReference<DiscloseListActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            DiscloseListActivity activity = mWrActivity.get();
//            if(activity == null){
//                return;
//            }
//            switch(msg.what){
//                case MEG_WHATE_RAISE_BTN:
//                    activity.raiseCreateDiscloseButton();
//                    break;
//                case MEG_WHATE_HIDE_BTN:
//                    activity.hideCreateDiscloseButton();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    public void onEventMainThread(ActionEvent event) {
//        if (event.getAction() == ActionEvent.ACTION_LOAD_DISCLOSE) {
//            L.i(TAG, "load disclose");
//            mDataList.clear();
//            mDataList.addAll(event.getDiscloseList());
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//}
