package com.bhsc.mobile.baseclass;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.WeakHandler;

/**
 * Created by lynn on 15-9-30.
 */
public class BaseActivity extends FragmentActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    private final int QUITE_BACKKEY_COUNT = 2;
    private final long BACKKEY_COUNT_RESET_DELAY = 5000;

    private int mBackKeyCount = 1;

    private WeakHandler mHandler = new WeakHandler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBackKeyCount >= QUITE_BACKKEY_COUNT) {
                mHandler.removeCallbacks(ResetBackKeyCount);
                finish();
            } else {
                Toast.makeText(BaseActivity.this, getResources().getString(R.string.caution_quite), Toast.LENGTH_SHORT).show();
                mBackKeyCount++;
                mHandler.postDelayed(ResetBackKeyCount, BACKKEY_COUNT_RESET_DELAY);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable ResetBackKeyCount = new Runnable(){
        @Override
        public void run() {
            L.i(TAG, "reset back key count!");
            mBackKeyCount = 1;
        }
    };
}
