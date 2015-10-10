package com.bhsc.mobile.main;


import android.app.Application;

import com.android.pc.ioc.app.Ioc;

/**
 * Created by lynn on 15-9-16.
 */
public class BHApplication extends Application {

    public static int screenWidth;
    public static int screenHeight;

    @Override
    public void onCreate() {
        Ioc.getIoc().init(this);
        super.onCreate();
    }
}
