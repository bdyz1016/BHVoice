package com.bhsc.userpages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bhsc.mobile.R;

/**
 * Created by lynn on 10/15/15.
 */
public class ResetPasswordActivity extends Activity implements View.OnClickListener{

    private View activity_reset_password_back;
    private EditText activity_reset_password;
    private EditText activity_reset_password_repeat;
    private Button activity_reset_password_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
    }

    private void initWidget(){
        activity_reset_password_back = findViewById(R.id.activity_reset_password_back);
        activity_reset_password = (EditText) findViewById(R.id.activity_reset_password);
        activity_reset_password_repeat = (EditText) findViewById(R.id.activity_reset_password_repeat);
        activity_reset_password_done = (Button) findViewById(R.id.activity_reset_password_done);
    }

    private void goBack(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_reset_password_back:
                goBack();
                break;
            default:
                break;
        }
    }
}
