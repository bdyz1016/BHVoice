package com.bhsc.mobile.disclose;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;


/**
 * Created by lynn on 15-9-29.
 */
@InjectLayer(R.layout.activity_disclose)
public class DiscloseActivity extends Activity {

    @InjectAll
    Views views;

    class Views{
        @InjectBinder(method = "menu", listeners = {OnClick.class})
        View fragment_disclose_title_menu;
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View fragment_disclose_title_back;
        EditText activity_disclose_edit_title, activity_disclose_edit_content;
        @InjectBinder(method = "confirm", listeners = {OnClick.class})
        View activity_disclose_edit_confirm;
        @InjectBinder(method = "cancel", listeners = {OnClick.class})
        View activity_disclose_edit_cancel;
        LinearLayout activity_disclose_edit_images;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void menu(){}

    private void goBack(){
        finish();
    }

    private void confirm(){
        finish();
    }

    private void cancel(){
        finish();
    }
}
