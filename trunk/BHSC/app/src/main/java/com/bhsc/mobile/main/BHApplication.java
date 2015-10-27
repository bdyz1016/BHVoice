package com.bhsc.mobile.main;


import android.app.Application;

import com.android.pc.ioc.app.Ioc;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lynn on 15-9-16.
 */
public class BHApplication extends Application {

    public static int screenWidth;
    public static int screenHeight;

    public static final String AppID = "wx1ede16f1495fe99b";
    public static final String AppSecret = "6b4538854f9dbe9921a68c2b59c474f9";

    @Override
    public void onCreate() {
        Ioc.getIoc().init(this);
        super.onCreate();
    }
}
