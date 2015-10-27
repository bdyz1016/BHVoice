package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.view.dialog.DefaultDialog;

/**
 * Created by lynn on 10/15/15.
 */
@InjectLayer(R.layout.activity_reset_password)
public class ResetPasswordActivity extends Activity {

    class Views{
        @InjectBinder(method = "goback", listeners = {OnClick.class})
        View activity_reset_password_back;
        EditText activity_reset_password;
        EditText activity_reset_password_repeat;
        @InjectBinder(method = "done", listeners = {OnClick.class})
        Button activity_reset_password_done;
    }

    @InjectAll
    private Views mViews;

    private DefaultDialog mDialog;

    private void goback(){
        finish();
    }

    private void done(){
        if(mDialog != null && mDialog .isShowing()){
            mDialog.dismiss();
        }
        mDialog = new DefaultDialog(this);
        mDialog.setMessage("密码修改成功");
        mDialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
            @Override
            public void onClick() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
