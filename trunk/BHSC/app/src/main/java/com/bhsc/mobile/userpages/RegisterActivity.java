package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.bhsc.mobile.userpages.event.UserEvent;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.view.BHRatioButton;


/**
 * Created by lynn on 10/15/15.
 */
@InjectLayer(R.layout.activity_register)
public class RegisterActivity extends Activity {
    private final String TAG = RegisterActivity.class.getSimpleName();

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_register_back;
        EditText activity_register_email;
        EditText activity_register_password;
        EditText activity_register_password_repeat;
        TextView activity_register_warning;
        @InjectBinder(method = "done", listeners = {OnClick.class})
        Button activity_register_done;
        TextView activity_register_agreement;
        BHRatioButton login_agreement;
    }

    @InjectAll
    Views mViews;

    private LayoutInflater mInflater;

    private int mAgreementColor;

    /**
     * 是否同意滨海视窗用户协议
     */
    private boolean mAgreement = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void init(){
        mInflater = LayoutInflater.from(this);

        mAgreementColor = ContextCompat.getColor(this, R.color.agreement_text);
        SpannableString spanString = new SpannableString("同意滨海视窗用户协议");
        spanString.setSpan(mClickableSpan, 2, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mViews.activity_register_agreement.setText(spanString);
        mViews.activity_register_agreement.setMovementMethod(LinkMovementMethod.getInstance());

        mViews.login_agreement.setOnCheckedChangeListener(new BHRatioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                mAgreement = isChecked;
            }
        });

        mViews.activity_register_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViews.login_agreement.setIsChecked(!mViews.login_agreement.isChecked());
            }
        });

        EventBus.getDefault().register(this);
    }

    private void done(){
        hideSoftInputMethod();

        String email = mViews.activity_register_email.getText().toString();
        String password = mViews.activity_register_password.getText().toString();
        String confirmPassword = mViews.activity_register_password_repeat.getText().toString();
        UserPresenter.getInstance(this).register(email, password, confirmPassword);
    }

    private void goBack(){
        finish();
    }

    private ClickableSpan mClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            L.i(TAG, "agreement clicked!");
            startActivity(new Intent(RegisterActivity.this, AgreementActivity.class));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mAgreementColor);
            ds.setUnderlineText(false);
        }
    };

    public void onEventMainThread(UserEvent event){
        if(event.getAction() == UserEvent.ACTION_REGISTER_SUCCESS){
            L.i(TAG,"login success!");
            finish();
        } else if(event.getAction() == UserEvent.ACTION_REGISTER_FAILED){
            L.i(TAG, "login failed!");
            switch (event.getErrorCode()){
                case UserEvent.ERROR_USERNAME_EMPTY:
                    break;
                case UserEvent.ERROR_USERNAME_FORMAT:
                    mViews.activity_register_email.setText("");
                    break;
                case UserEvent.ERROR_PASSWORD_EMPTY:
                    break;
                case UserEvent.ERROR_PASSWORD_FORMAT:
                    mViews.activity_register_password.setText("");
                    mViews.activity_register_password_repeat.setText("");
                    break;
                case UserEvent.ERROR_USERNAME_EXIST:
                    mViews.activity_register_email.setText("");
                    mViews.activity_register_password.setText("");
                    mViews.activity_register_password_repeat.setText("");
                    break;
                case UserEvent.ERROR_PASSWORD_INCONSISTENT:
                    mViews.activity_register_password.setText("");
                    mViews.activity_register_password_repeat.setText("");
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
                mViews.activity_register_warning.setText(warningText);
                shake(mViews.activity_register_warning);
            }
            displayLoginFailedDialog();
        }
    }

    private void displayLoginFailedDialog(){
        View view = mInflater.inflate(R.layout.dialog_register_failed, null);
        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void shake(View v){
        v.clearAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        v.startAnimation(shake);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mViews.activity_register_password_repeat.getWindowToken(), 0);
    }
}
