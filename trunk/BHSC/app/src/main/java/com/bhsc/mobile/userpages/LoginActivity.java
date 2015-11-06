package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.dataclass.Data_DB_User;
import com.bhsc.mobile.userpages.event.UserEvent;
import com.bhsc.mobile.utils.L;

/**
 * Created by lynn on 10/14/15.
 */
@InjectLayer(R.layout.activity_login)
public class LoginActivity extends Activity{
    private final String TAG = LoginActivity.class.getSimpleName();

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_login_back;
        @InjectBinder(method = "register", listeners = {OnClick.class})
        View activity_login_register;
        EditText activity_login_username;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void init(){
        mInflater = LayoutInflater.from(LoginActivity.this);
        mCurrentUser = new Data_DB_User();

        mViews.activity_login_username.addTextChangedListener(mEmailWatcher);
        mViews.activity_login_password.addTextChangedListener(mPasswdWatcher);

        EventBus.getDefault().register(this);
    }

    private void login(){
        hideSoftInputMethod();

        String userName = mViews.activity_login_username.getText().toString();
        String password =  mViews.activity_login_password.getText().toString();

        mCurrentUser.setPassword(password);
        mCurrentUser.setUsername(userName);

        UserPresenter.getInstance(this).login(userName, password);
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

    public void onEventMainThread(UserEvent event){
        if(event.getAction() == UserEvent.ACTION_LOGIN_SUCCESS){
            L.i(TAG,"login success!");
            finish();
        } else if(event.getAction() == UserEvent.ACTION_LOGIN_FAILED){
            L.i(TAG, "login failed!");
            switch (event.getErrorCode()){
                case UserEvent.ERROR_USERNAME_EMPTY:
                    break;
                case UserEvent.ERROR_USERNAME_FORMAT:
                    mViews.activity_login_username.setText("");
                    break;
                case UserEvent.ERROR_USERNAME_WRONG:
                    mViews.activity_login_password.setText("");
                    mViews.activity_login_username.setText("");
                    break;
                case UserEvent.ERROR_PASSWORD_EMPTY:
                    break;
                case UserEvent.ERROR_PASSWORD_FORMAT:
                    mViews.activity_login_password.setText("");
                    break;
                case UserEvent.ERROR_PASSWORD_WRONG:
                    mViews.activity_login_password.setText("");
                    break;
            }
            String warningText;
            try {
                warningText = (String) event.getExtra();
            } catch (ClassCastException e){
                e.printStackTrace();
                warningText = this.getString(R.string.login_error_2);
            }
            if(!TextUtils.isEmpty(warningText)) {
                mViews.activity_login_warning.setText(warningText);
                shake(mViews.activity_login_warning);
            }
            displayLoginFailedDialog();
        }
    }

    private void shake(View v){
        v.clearAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        v.startAnimation(shake);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mViews.activity_login_password.getWindowToken(), 0);
    }

    private TextWatcher mEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher mPasswdWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
