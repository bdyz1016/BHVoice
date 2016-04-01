package com.bhsc;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orm.SugarContext;

/**
 * Created by zhanglei on 16/3/29.
 */
public class MyApplication extends Application {

    public static int screenWidth;
    public static int screenHeight;

    public static final String Address = "http://101.200.209.6";

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Fresco.initialize(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
