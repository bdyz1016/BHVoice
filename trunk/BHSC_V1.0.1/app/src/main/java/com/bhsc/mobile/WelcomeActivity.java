package com.bhsc.mobile;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;


/**
 * Created by lynn on 15-10-8.
 */
public class WelcomeActivity extends Activity {

    private Thread mThread;
    private Thread msKipThread;
    private long mDelay = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (shouldInit()) {
            MiPushClient.registerPush(getApplicationContext(), MyApplication.APP_ID_MI, MyApplication.APP_KEY_MI);
        }

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(MyApplication.TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(MyApplication.TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

        mThread = new Thread(AsyncInitialization);
        mThread.start();

        msKipThread = new Thread(SkipMainActivity);
        msKipThread.start();
    }

    private void initGlobalData() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MyApplication.screenWidth = displayMetrics.widthPixels;
        MyApplication.screenHeight = displayMetrics.heightPixels;
//        UserManager.getInstance(this).getCurrentUser();
    }

    private Runnable AsyncInitialization = new Runnable() {
        @Override
        public void run() {
            initGlobalData();
        }
    };

    private Runnable SkipMainActivity = new Runnable() {
        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                mThread.join();
                long endTime = System.currentTimeMillis();
                long interval = endTime - startTime;
                long time = mDelay - interval;
                if(time > 0) {
                    Thread.sleep(time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
