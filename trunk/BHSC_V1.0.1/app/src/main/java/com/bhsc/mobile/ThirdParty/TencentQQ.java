package com.bhsc.mobile.ThirdParty;

import android.app.Activity;
import android.os.Bundle;

import com.bhsc.mobile.utils.L;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by lynn on 15-10-11.
 */
public class TencentQQ {
    private final String TAG = TencentQQ.class.getSimpleName();

    public static final String APPID = "1104901688";

    private static class SingletonHolder{
        public static TencentQQ sTencentQQ = new TencentQQ();
    }

    public static TencentQQ getInstance(Activity activity){
        SingletonHolder.sTencentQQ.init(activity);
        return SingletonHolder.sTencentQQ;
    }

    private Tencent mTencent;
    private Activity mContext;

    private TencentQQ(){}

    private void init(Activity activity){
        this.mContext = activity;
        this.mTencent = Tencent.createInstance(APPID, activity);
    }

    public void shareToQQ(Bundle params, IUiListener listener) {
        mTencent.shareToQQ(mContext, params, listener);
    }

    public void shareToQZone(Bundle params, IUiListener listener){
        mTencent.shareToQzone(mContext, params, listener);
    }

    public void login(){
        L.i(TAG, "login");
        if (!mTencent.isSessionValid()) {
            L.i(TAG, "Session is not Valid");
            mTencent.login(mContext, "all", loginListener);
        } else {
            mTencent.logout(mContext);
        }
    }

    private IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            L.i(TAG, "doComplete");
//            if (!mTencent.isSessi
//            initOpenidAndToken(values);
//            updateUserInfo();
//            updateLoginButton();
        }
    };

    private abstract class BaseUiListener implements IUiListener {

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
//            handlePrizeShare();
            doComplete((JSONObject) response);
        }

        protected abstract void doComplete(JSONObject values);

        @Override
        public void onError(UiError e) {
            L.i(TAG, "onError:" + e.errorDetail);
            Util.showResultDialog(mContext, "onError" ,e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.showResultDialog(mContext, "onCancel", "failed");
            Util.dismissDialog();
        }
    }
}