package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.accessory.FeedbackActivity;
import com.bhsc.mobile.accessory.FestivalActivity;
import com.bhsc.mobile.dataclass.Data_DB_Discuss;
import com.bhsc.mobile.dataclass.Data_DB_User;
import com.bhsc.mobile.disclose.DiscloseActivity;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.discuss.DiscussActivity;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.accessory.NotificationActivity;
import com.bhsc.mobile.settings.SettingsActivity;
import com.bhsc.mobile.userpages.event.UserEvent;
import com.bhsc.mobile.utils.L;

import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class UserFragment extends Fragment{
    private final String TAG = UserFragment.class.getSimpleName();
    @InjectAll
    private Views mViews;

    class Views{
        @InjectBinder(method = "userInfo", listeners = {OnClick.class})
        ImageView fragment_user_photo;
        @InjectBinder(method = "userDiscuss", listeners = {OnClick.class})
        View fragment_user_discuss;
        @InjectBinder(method = "version", listeners = {OnClick.class})
        View fragment_user_versions;
        @InjectBinder(method = "settings", listeners = {OnClick.class})
        View fragment_user_settings;
        @InjectBinder(method = "notification", listeners = {OnClick.class})
        View fragment_user_notify;
        @InjectBinder(method = "activity", listeners = {OnClick.class})
        View fragment_user_activity;
        @InjectBinder(method = "disclose", listeners = {OnClick.class})
        View fragment_user_disclose;
        @InjectBinder(method = "feedback", listeners = {OnClick.class})
        View fragment_user_feedback;
        TextView fragment_user_name;
        MyListView fragment_user_discuss_list;
    }

    private View mContentView;
    private Context mContext;
    private DiscussAdapter mAdapter;
    private List<Data_DB_Discuss> mDiscusses;
    private UserManager mUserManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_user, container, false);
        Handler_Inject.injectFragment(this, mContentView);
        EventBus.getDefault().register(this);
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void initData(){
        UserPresenter.getInstance(mContext).initUserData();
        mUserManager = UserManager.getInstance(mContext);
    }

    private void userInfo(){
        if(UserManager.getInstance(mContext).isLogined()) {
            Intent intent = new Intent();
            intent.setClass(mContext, UserInfoActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, LoginAndRegisterActivity.class);
            startActivity(intent);
        }
    }

    private void userDiscuss(){
        if(mUserManager.isLogined()){
            Intent intent = new Intent();
            intent.setClass(mContext, DiscussActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
        }
    }

    private void version(){}

    private void settings(){
        Intent intent = new Intent();
        intent.setClass(mContext, SettingsActivity.class);
        startActivity(intent);
    }

    private void notification(){
        Intent intent = new Intent();
        intent.setClass(mContext, NotificationActivity.class);
        startActivity(intent);
    }

    private void activity(){
        Intent intent = new Intent();
        intent.setClass(mContext, FestivalActivity.class);
        startActivity(intent);
    }

    private void disclose(){
        if(mUserManager.isLogined()){
            Intent intent = new Intent();
            intent.setClass(mContext, DiscloseActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "请先登录!", Toast.LENGTH_SHORT).show();
        }
    }

    private void feedback(){
        Intent intent = new Intent();
        intent.setClass(mContext, FeedbackActivity.class);
        startActivity(intent);
    }

    public void onEventMainThread(DiscussEvent event){
//        List<Data_DB_Discuss> discusses = event.getDiscusses();
//        L.i(TAG, "刷新评论列表:" + discusses.size());
//        if(mAdapter == null){
//            mDiscusses = discusses;
//            mAdapter = new DiscussAdapter(mContext, mDiscusses);
//            mViews.fragment_user_discuss_list.setAdapter(mAdapter);
//        } else {
//            mDiscusses.clear();
//            mDiscusses.addAll(discusses);
//            mAdapter.notifyDataSetChanged();
//        }
    }
    public void onEventMainThread(UserEvent event){
        if(event.getAction() == UserEvent.ACTION_GET_USERINFO){
            Data_DB_User user = (Data_DB_User) event.getExtra();
            if(user == null){
                return;
            }
            mViews.fragment_user_name.setText(user.getNickName());
        }
    }
}
