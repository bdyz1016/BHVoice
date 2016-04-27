package com.bhsc.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;


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
}
