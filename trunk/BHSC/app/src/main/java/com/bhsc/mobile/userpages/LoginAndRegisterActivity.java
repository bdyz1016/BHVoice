package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
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
 * Created by lynn on 10/14/15.
 */
@InjectLayer(R.layout.activity_loginandregister)
public class LoginAndRegisterActivity extends Activity {
    private final String TAG = LoginAndRegisterActivity.class.getSimpleName();

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

    private Tencent mTencent;

    private void login(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void register(){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void loginByQQ(){
        L.i(TAG, "loginByQQ");
        mTencent = Tencent.createInstance(TencentQQ.APPID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            L.i(TAG, "Session is not Valid");
            mTencent.login(this, "all", loginListener);
        } else {
            mTencent.logout(LoginAndRegisterActivity.this);
        }
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
                Util.showResultDialog(LoginAndRegisterActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(LoginAndRegisterActivity.this, "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(LoginAndRegisterActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage((Activity)LoginAndRegisterActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginAndRegisterActivity.this, "onCancel: ");
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
            UserInfo mInfo = new UserInfo(LoginAndRegisterActivity.this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {
        }
    }
}
