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

    public static final String Address = "http://101.200.209.6";

    @Override
    public void onCreate() {
        Ioc.getIoc().init(this);
        super.onCreate();
    }
}
