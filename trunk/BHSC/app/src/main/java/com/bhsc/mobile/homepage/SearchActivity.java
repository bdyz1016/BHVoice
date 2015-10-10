package com.bhsc.mobile.homepage;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.disclose.views.MyListView;

/**
 * Created by lynn on 15-10-8.
 */
@InjectLayer(R.layout.activity_search)
public class SearchActivity extends Activity {

    @InjectAll
    private Views mViews;

    class Views{
        EditText activity_search_keyword;
        @InjectBinder(method = "cancel", listeners = {OnClick.class})
        Button activity_search_cancel;
        MyListView activity_search_list;
        View activity_search_hint;
    }

    @InjectInit
    private void init(){

    }

    private void cancel(){
        finish();
    }
}
