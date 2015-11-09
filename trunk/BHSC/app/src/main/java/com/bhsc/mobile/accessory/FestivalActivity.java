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
@InjectLayer(R.layout.activity_festival)
public class FestivalActivity extends Activity {

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_festival_back;
        MyListView activity_festival_list;
        TextView empty;
    }

    @InjectAll
    private Views mViews;

    @InjectInit
    private void init(){
        mViews.activity_festival_list.setEmptyView(mViews.empty);
    }

    private void goBack(){
        finish();
    }
}
