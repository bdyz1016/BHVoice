package com.bhsc.mobile.settings;

import android.app.Activity;
import android.view.View;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;

/**
 * Created by lynn on 11/5/15.
 */
@InjectLayer(R.layout.activity_settings)
public class SettingsActivity extends Activity {

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View fragment_settings_title_back;
    }

    @InjectAll
    private Views mViews;

    private void goBack(){
        finish();
    }
}
