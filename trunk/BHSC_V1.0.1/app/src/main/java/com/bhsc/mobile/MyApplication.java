package com.bhsc.mobile;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orm.SugarContext;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import co.lujun.tpsharelogin.TPManager;

/**
 * Created by zhanglei on 16/3/29.
 */
public class MyApplication extends Application {

    public static final String TAG = "com.bhsc.mobile";

    public static int screenWidth;
    public static int screenHeight;

    public static int DISCLOSE_IMAGE_RESIZE = 100;

    public static final String APP_ID_QQ = "1104901688";
    public static final String APP_KEY_QQ = "m9QhSkm6yOKs6YZ7";

    public static final String APP_ID_WECHAT = "wx1ede16f1495fe99b";
    public static final String APP_SECRET_WECHAT = "fba6a41be329981365829de421f37710";

    public static final String APP_ID_MI = "2882303761517479944";
    public static final String APP_KEY_MI = "5671747999944";
    public static final String APP_SECRET_MI = "0CA+1hQZUnoiC+PJ6frcCA==";
    public static final String CHANNEL = "test";

    public static final String Address = "http://101.200.209.6";

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Fresco.initialize(this);
        //参数分别为微博回调地址、微博APP KEY、微博APP SECRET、QQ APPID、QQ APPSECRET、微信APPID、微信APPSECRET
        TPManager.getInstance().initAppConfig(
                "", "", "",
                APP_ID_QQ, "",
                APP_ID_WECHAT, APP_SECRET_WECHAT);
        MiStatInterface.initialize(this, APP_ID_MI, APP_KEY_MI, CHANNEL);
        MiStatInterface.enableExceptionCatcher(true);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
