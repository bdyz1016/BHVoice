package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.TencentQQ;
import com.bhsc.mobile.ThirdParty.Util;
import com.bhsc.mobile.utils.L;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lynn on 10/15/15.
 */
public class LoginAndRegisterFragment extends Fragment {
    private final String TAG = LoginAndRegisterFragment.class.getSimpleName();

    private final String SCOPE = "all";

    class Views{
        @InjectBinder(method = "login", listeners = {OnClick.class})
        Button activity_btn_login;
        @InjectBinder(method = "register", listeners = {OnClick.class})
        Button activity_btn_register;
        @InjectBinder(method = "loginByQQ", listeners = {OnClick.class})
        ImageView login_by_qq;
    }
    @InjectAll
    private Views mVies;

    private View mContentView;

    private Context mContext;

    private Tencent mTencent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_loginandregister, container, false);
        Handler_Inject.injectFragment(this, mContentView);
        return mContentView;
    }

    private void login(){
        startActivity(new Intent(mContext, LoginActivity.class));
    }

    private void register(){
        startActivity(new Intent(mContext, RegisterActivity.class));
    }

    private void loginByQQ(){
        L.i(TAG,"loginByQQ");
        startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
    }
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            L.i(TAG, values.toString());
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(mContext, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(mContext, "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(mContext, response.toString(), "登录成功");
            // 有奖分享处理
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage((Activity)mContext, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage((Activity)mContext, "onCancel: ");
            Util.dismissDialog();
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }
                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    if (json.has("figureurl")) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                        } catch (JSONException e) {

                        }
                    }
                }

                @Override
                public void onCancel() {
                }
            };
            UserInfo mInfo = new UserInfo(mContext, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
        }
    }
}
