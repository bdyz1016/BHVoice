package com.bhsc.mobile.accessory;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.disclose.views.MyListView;

/**
 * Created by lynn on 11/5/15.
 */
@InjectLayer(R.layout.activity_notification)
public class NotificationActivity extends Activity {

    class Views{
        MyListView activity_notification_list;
        TextView empty;
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_notification_back;
    }

    @InjectAll
    private Views mViews;

    @InjectInit
    private void init(){
        mViews.activity_notification_list.setEmptyView(mViews.empty);
    }

    private void goBack(){
        finish();
    }
}
