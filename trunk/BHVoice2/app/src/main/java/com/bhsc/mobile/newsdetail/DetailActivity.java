package com.bhsc.mobile.newsdetail;

import android.app.Activity;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bhsc.mobile.R;

/**
 * Created by lynn on 15-9-19.
 */
@InjectLayer(R.layout.activity_newsdetail)
public class DetailActivity extends Activity {

    @InjectInit
    private void initView(){

    }
}
