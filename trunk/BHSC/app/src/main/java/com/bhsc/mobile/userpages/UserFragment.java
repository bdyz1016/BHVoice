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

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.datalcass.Data_DB_User;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.userpages.event.UserEvent;
import com.bhsc.mobile.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class UserFragment extends Fragment{
    private final String TAG = UserFragment.class.getSimpleName();

    private View mContentView;
    private Context mContext;
    private DiscussAdapter mAdapter;
    private List<Data_DB_Discuss> mDiscusses;

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
        TextView fragment_user_name;
        MyListView fragment_user_discuss_list;
    }

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

    private void userDiscuss(){}

    private void version(){}

    private void settings(){}

    public void onEventMainThread(DiscussEvent event){
        List<Data_DB_Discuss> discusses = event.getDiscusses();
        L.i(TAG, "刷新评论列表:" + discusses.size());
        if(mAdapter == null){
            mDiscusses = discusses;
            mAdapter = new DiscussAdapter(mContext, mDiscusses);
            mViews.fragment_user_discuss_list.setAdapter(mAdapter);
        } else {
            mDiscusses.clear();
            mDiscusses.addAll(discusses);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void onEventMainThread(UserEvent event){
        if(event.getAction() == UserEvent.ACTION_GET_USERINFO){
            Data_DB_User user = (Data_DB_User) event.getExtra();
            if(user == null){
                return;
            }
            mViews.fragment_user_name.setText(user.getNickName());
//            ImageUtil.getInstance().loadBitmap(user.getPhotoPath(), mViews.fragment_user_photo, mViews.fragment_user_photo.getWidth(), mViews.fragment_user_photo.getHeight());
        }
    }
}
