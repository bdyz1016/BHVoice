package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.internet.AjaxCallBack;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.annotation.Email;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_User;
import com.bhsc.mobile.main.event.MainFrameEvent;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.utils.L;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

/**
 * Created by lynn on 10/14/15.
 */
@InjectLayer(R.layout.activity_login)
public class LoginActivity extends Activity implements Validator.ValidationListener{
    private final String TAG = LoginActivity.class.getSimpleName();

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_login_back;
        @InjectBinder(method = "register", listeners = {OnClick.class})
        View activity_login_register;
        @Email(empty = false, message = "邮箱格式错误", order = 3)
        EditText activity_login_username;
        @Password(message = "请输入密码", maxLength = 18, minLength = 6,order = 1)
        EditText activity_login_password;
        TextView activity_login_warning;
        @InjectBinder(method = "forgetPassword", listeners = {OnClick.class})
        View activity_login_forget_password;
        @InjectBinder(method = "login" ,listeners = {OnClick.class})
        Button activity_login_login;

        TextView login_error_1;
    }

    @InjectAll
    private Views mViews;

    private Data_DB_User mCurrentUser;

    private LayoutInflater mInflater;

    private Validator validator;

    @InjectInit
    private void init(){
        mInflater = LayoutInflater.from(LoginActivity.this);
        mCurrentUser = new Data_DB_User();
    }

    private void login(){
        String userName = mViews.activity_login_username.getText().toString();
        String password =  mViews.activity_login_password.getText().toString();

        mCurrentUser.setPassword(password);
        mCurrentUser.setUserName(userName);

//        LinkedHashMap<String, String> params = new LinkedHashMap<>();
//        params.put("loginId", userName);
//        params.put("password", password);
//        FastHttp.ajaxForm("http://120.24.51.153/news/user/login", params, new AjaxCallBack() {
//            @Override
//            public boolean stop() {
//                return false;
//            }
//
//            @Override
//            public void callBack(ResponseEntity responseEntity) {
//                L.i(TAG, responseEntity.toString());
//            }
//        });

        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.validate();
    }

    private void forgetPassword(){
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    private void register(){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void goBack(){
        finish();
    }

    private void displayLoginFailedDialog(){
        View view = mInflater.inflate(R.layout.dialog_loginfailed, null);
        Toast toast = new Toast(LoginActivity.this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void onValidationSucceeded() {
        UserManager.getInstance().setIsLogined(true);
        EventBus.getDefault().post(new MainFrameEvent(MainFrameEvent.ACTION_LOGINED));
        finish();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        mViews.login_error_1.setText(message);
        displayLoginFailedDialog();
    }
}
