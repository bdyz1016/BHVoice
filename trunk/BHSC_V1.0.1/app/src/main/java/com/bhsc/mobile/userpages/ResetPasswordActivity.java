package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.net.RequestError;

/**
 * Created by lynn on 10/15/15.
 */
public class ResetPasswordActivity extends Activity {

    private View activity_reset_password_back;
    private EditText activity_reset_password;
    private EditText activity_reset_password_repeat;
    private Button activity_reset_password_done;

    private UserManager mUserManager;
    private Context mContext;

    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    private void initWidget(){
        activity_reset_password_back = findViewById(R.id.activity_reset_password_back);
        activity_reset_password_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        activity_reset_password = (EditText) findViewById(R.id.activity_reset_password);
        activity_reset_password_repeat = (EditText) findViewById(R.id.activity_reset_password_repeat);
        activity_reset_password_done = (Button) findViewById(R.id.activity_reset_password_done);
        activity_reset_password_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgressDialog(null);
                mUserManager.resetPassword(activity_reset_password.getText().toString(), activity_reset_password_repeat.getText().toString(), new UserManager.OnEditUserInfoListener() {
                    @Override
                    public void success() {
                        dismissProgressDialog();
                        Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void failed(RequestError error) {
                        dismissProgressDialog();
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initData(){
        mUserManager = new UserManager(this);
        mContext = this;
    }

    private void goBack(){
        finish();
    }

    private void displayProgressDialog(String message){
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        if(!TextUtils.isEmpty(message)){
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    private void dismissProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
