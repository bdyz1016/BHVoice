package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.ThirdParty.Util;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.model.QQLoginResponse;
import com.bhsc.mobile.ThirdParty.model.WechatLoginResponse;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.bhsc.mobile.userpages.model.UserResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.Gson;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import co.lujun.tpsharelogin.listener.StateListener;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;

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

    private Context mContext;

//    private Tencent mTencent;
//    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginandregister);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    private void initWidget() {
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

    private void initData() {
        mContext = this;
    }

    private void login() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void loginByWechat() {
        L.i(TAG, "loginByWechat");
//        if(mIWXAPI == null){
//            mIWXAPI =  WXAPIFactory.createWXAPI(this, WeChatShare.AppID, true);
//            mIWXAPI.registerApp(WeChatShare.AppID);
//        }
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "bhsc";
//        mIWXAPI.sendReq(req);
        WXManager wxManager = new WXManager(this);
        StateListener<String> weChatStateListener = new StateListener<String>() {
            @Override
            public void onComplete(String s) {
                L.d(TAG, s);
                if(Method.isJSONValid(s)) {
                    new SaveWechatUserInfo().execute(s);
                }
            }

            @Override
            public void onError(String err) {
                L.d(TAG, err);
                Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                L.d(TAG, "onCancel()");
            }
        };
        wxManager.setListener(weChatStateListener);
        //微信登录
        wxManager.onLoginWithWX();
    }

    private void loginByQQ() {
        L.i(TAG, "loginByQQ");
//        mTencent = Tencent.createInstance(TencentQQ.APPID, this.getApplicationContext());
//        if (!mTencent.isSessionValid()) {
//            L.i(TAG, "Session is not Valid");
//            mTencent.login(this, "all", loginListener);
//        } else {
//            mTencent.logout(LoginAndRegisterActivity.this);
//        }
//        finish();
        QQManager qqManager = new QQManager(this);
        StateListener<String> qqStateListener = new StateListener<String>() {
            @Override
            public void onComplete(String s) {
                L.d(TAG, s);
                if(Method.isJSONValid(s)) {
                    new SaveQQUserInfo().execute(s);
                }
            }

            @Override
            public void onError(String err) {
                L.d(TAG, err);
                Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                L.d(TAG, "onCancel()");
            }
        };
        qqManager.setListener(qqStateListener);
        qqManager.onLoginWithQQ();
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
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage((Activity) LoginAndRegisterActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginAndRegisterActivity.this, "onCancel: ");
            Util.dismissDialog();
        }
    }

    private void updateUserInfo() {
//        if (mTencent != null && mTencent.isSessionValid()) {
//            IUiListener listener = new IUiListener() {
//
//                @Override
//                public void onError(UiError e) {
//
//                }
//                @Override
//                public void onComplete(final Object response) {
//                    JSONObject json = (JSONObject) response;
//                    if (json.has("figureurl")) {
//                        Bitmap bitmap = null;
//                        try {
//                            bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
//                        } catch (JSONException e) {
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancel() {
//                }
//            };
//            UserInfo mInfo = new UserInfo(LoginAndRegisterActivity.this, mTencent.getQQToken());
//            mInfo.getUserInfo(listener);
//        } else {
//        }
    }

    private class SaveWechatUserInfo extends AsyncTask<String, Void, Integer> {
        private Gson mGson;

        public SaveWechatUserInfo() {
            mGson = new Gson();
        }

        @Override
        protected Integer doInBackground(String... params) {
            if (params.length == 0) {
                return -1;
            }
            String param = params[0];
            final WechatLoginResponse response = mGson.fromJson(param, WechatLoginResponse.class);
            if (response != null) {
                RequestFuture<String> requestFuture = RequestFuture.newFuture();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MyApplication.Address + "/user/thirdLogin", requestFuture, requestFuture){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("openId", response.getVerify_data().getOpenid());
                        params.put("type", "weixin");
                        return params;
                    }
                };
                MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
                try {
                    String result = requestFuture.get(30, TimeUnit.SECONDS);
                    if (TextUtils.isEmpty(result)) {
                        return -1;
                    }
                    UserResponse userResponse = mGson.fromJson(result, UserResponse.class);
                    if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                        Data_DB_User userInfo = userResponse.getUser();
                        userInfo.setHeadurl(response.getUser_data().getHeadimgurl());
//                      userInfo.setNickName(response.getUser_data().getNickname());
                        userInfo.setUsername(response.getUser_data().getNickname());
                        if (response.getUser_data().getSex().equals("男")) {
                            userInfo.setGender(Data_DB_User.GENDER_MALE);
                        } else {
                            userInfo.setGender(Data_DB_User.GENDER_FEMALE);
                        }
                        UserManager.login(userInfo);
                        return 0;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == null || integer < 0) {
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    private class SaveQQUserInfo extends AsyncTask<String, Void, Integer> {
        private Gson mGson;

        public SaveQQUserInfo() {
            mGson = new Gson();
        }

        @Override
        protected Integer doInBackground(final String... params) {
            if (params.length == 0) {
                return -1;
            }
            String param = params[0];
            final QQLoginResponse response = mGson.fromJson(param, QQLoginResponse.class);
            if (response != null) {
                RequestFuture<String> requestFuture = RequestFuture.newFuture();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MyApplication.Address + "/user/thirdLogin", requestFuture, requestFuture){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("openId", response.getVerify_data().getOpenid());
                        params.put("type", "qq");
                        return params;
                    }
                };
                MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
                try {
                    String result = requestFuture.get(30, TimeUnit.SECONDS);
                    if (TextUtils.isEmpty(result)) {
                        return -1;
                    }
                    UserResponse userResponse = mGson.fromJson(result, UserResponse.class);
                    if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                        Data_DB_User userInfo = userResponse.getUser();
                        if (response.getUser_data().getGender().equals("男")) {
                            userInfo.setGender(Data_DB_User.GENDER_MALE);
                        } else {
                            userInfo.setGender(Data_DB_User.GENDER_FEMALE);
                        }
                        userInfo.setNickName(response.getUser_data().getNickname());
                        userInfo.setHeadurl(response.getUser_data().getFigureurl_qq_1());
                        userInfo.setUsername(response.getUser_data().getNickname());
                        UserManager.login(userInfo);
                        return 0;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == null || integer < 0) {
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }
}
