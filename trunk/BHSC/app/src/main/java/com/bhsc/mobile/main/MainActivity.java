package com.bhsc.mobile.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.WeChatShare;
import com.bhsc.mobile.baseclass.BaseActivity;
import com.bhsc.mobile.disclose.DiscloseFragment;
import com.bhsc.mobile.homepage.HomeFragment;
import com.bhsc.mobile.main.event.MainFrameEvent;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.userpages.LoginAndRegisterFragment;
import com.bhsc.mobile.userpages.UserFragment;
import com.bhsc.mobile.utils.L;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lynn on 15-9-16.
 */
@InjectLayer(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    class Views {
        public NavigationMenu activity_main_NavigationMenu;
        public ImageView Navigation_btn_1_icon, Navigation_btn_2_icon, Navigation_btn_3_icon;
        public TextView Navigation_btn_1_text, Navigation_btn_2_text, Navigation_btn_3_text;
    }


    private String mCurrentTab;

    private final int FRAGMENT_HOME = 0;
    private final int FRAGMENT_DISCLOSE = 1;
    private final int FRAGMENT_USER = 2;

    private Fragment[] mFragments;
    private int mPosition = 0;

    @InjectAll
    Views views;

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void init() {
        L.i(TAG, "init");
//        getSupportFragmentManager().beginTransaction().replace(R.id.activty_main_container, new HomeFragment()).commit();
        views.activity_main_NavigationMenu.setOnMenuItemClickListener(mMenuItemClickListener);

        mFragments = new Fragment[3];
        mFragments[0] = new HomeFragment();
        mFragments[1] = new DiscloseFragment();
        mFragments[2] = new UserFragment();

        switchHome();
        regToWX();//注册到微信
    }

    private NavigationMenu.OnMenuItemClickListener mMenuItemClickListener = new NavigationMenu.OnMenuItemClickListener() {
        @Override
        public void onItemClick(int id) {
            L.i(TAG, "onItemClick:" + id);
            switch (id) {
                case 0:
                    switchHome();
                    break;
                case 1:
                    switchDisclose();
                    break;
                case 2:
                    switchProfile();
                    break;
                default:
                    break;
            }
        }
    };

//    private void switchView(Fragment fragment) {
//        L.i(TAG, "switchView");
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.activty_main_container, fragment, fragment.getClass().getName()).commitAllowingStateLoss();
//    }

    private void switchView(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(mFragments[mPosition]);
        if (!mFragments[position].isAdded()) {
            fragmentTransaction.add(R.id.activty_main_container, mFragments[position]);
        }
        mPosition = position;
        fragmentTransaction.show(mFragments[position]);
        fragmentTransaction.commit();
    }

    private void switchHome() {
        if (!TextUtils.isEmpty(mCurrentTab) && mCurrentTab.equals(HomeFragment.class.getSimpleName())) {
            return;
        }
        mCurrentTab = HomeFragment.class.getSimpleName();
        views.Navigation_btn_1_icon.setBackgroundResource(R.mipmap.btn_home_press);
        views.Navigation_btn_2_icon.setBackgroundResource(R.mipmap.btn_disclose_normal);
        views.Navigation_btn_3_icon.setBackgroundResource(R.mipmap.btn_profile_nomal);
//        switchView(new HomeFragment());
        switchView(FRAGMENT_HOME);
    }

    private void switchDisclose() {
        if (!TextUtils.isEmpty(mCurrentTab) && mCurrentTab.equals(DiscloseFragment.class.getSimpleName())) {
            return;
        }
        mCurrentTab = DiscloseFragment.class.getSimpleName();
        views.Navigation_btn_1_icon.setBackgroundResource(R.mipmap.btn_home_normal);
        views.Navigation_btn_2_icon.setBackgroundResource(R.mipmap.btn_disclose_press);
        views.Navigation_btn_3_icon.setBackgroundResource(R.mipmap.btn_profile_nomal);
//        switchView(new DiscloseFragment());
        switchView(FRAGMENT_DISCLOSE);
    }

    private void switchProfile() {
        if (!TextUtils.isEmpty(mCurrentTab) && mCurrentTab.equals(UserFragment.class.getSimpleName())) {
            return;
        }
        mCurrentTab = UserFragment.class.getSimpleName();
        views.Navigation_btn_1_icon.setBackgroundResource(R.mipmap.btn_home_normal);
        views.Navigation_btn_2_icon.setBackgroundResource(R.mipmap.btn_disclose_normal);
        views.Navigation_btn_3_icon.setBackgroundResource(R.mipmap.btn_profile_press);
//        switchView(new UserFragment());
        switchView(FRAGMENT_USER);
    }

//    private void switchLogin() {
//        if (!TextUtils.isEmpty(mCurrentTab) && mCurrentTab.equals(LoginAndRegisterFragment.class.getSimpleName())) {
//            return;
//        }
//        mCurrentTab = LoginAndRegisterFragment.class.getSimpleName();
//        views.Navigation_btn_1_icon.setBackgroundResource(R.mipmap.btn_home_normal);
//        views.Navigation_btn_2_icon.setBackgroundResource(R.mipmap.btn_disclose_normal);
//        views.Navigation_btn_3_icon.setBackgroundResource(R.mipmap.btn_profile_press);
//        switchView(new LoginAndRegisterFragment());
//    }

    private void regToWX() {
        api = WXAPIFactory.createWXAPI(this, WeChatShare.AppID, true);
        api.registerApp(WeChatShare.AppID);
    }

    public void onEventMainThread(MainFrameEvent event) {
        switch(event.getAction()){
            case MainFrameEvent.ACTION_LOGINED:
                switchProfile();
                break;
            case MainFrameEvent.ACTION_LOGOUT:
                switchHome();
                break;
            default:
                break;
        }
    }
}
