package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bhsc.mobile.ThirdParty.TencentQQ;
import com.bhsc.mobile.ThirdParty.Util;
import com.bhsc.mobile.ThirdParty.WeChatShare;
import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.L;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lynn on 10/14/15.
 */
public class LoginAndRegisterActivity extends Activity {
    private final String TAG = LoginAndRegisterActivity.class.getSimpleName();

    private Button activity_btn_login;
    private Button activity_btn_register;
    private ImageView login_by_qq;
    private ImageView login_by_wechat;
    private View mBack;

    private Tencent mTencent;
    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginandregister);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
    }

    private void initWidget(){
        activity_btn_login = (Button) findViewById(R.id.activity_btn_login);
        activity_btn_register = (Button) findViewById(R.id.activity_btn_register);
        login_by_qq = (ImageView) findViewById(R.id.login_by_qq);
        login_by_wechat = (ImageView) findViewById(R.id.login_by_wechat);
        mBack = findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activity_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        activity_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        login_by_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByQQ();
            }
        });
        login_by_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByWechat();
            }
        });
    }

    private void login(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void register(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void loginByWechat(){
        L.i(TAG, "loginByWechat");
        if(mIWXAPI == null){
            mIWXAPI =  WXAPIFactory.createWXAPI(this, WeChatShare.AppID, true);
            mIWXAPI.registerApp(WeChatShare.AppID);
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "bhsc";
        mIWXAPI.sendReq(req);
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
        finish();
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
