package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.internet.AjaxCallBack;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.L;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

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
        EditText activity_register_nickname;
        EditText activity_register_password;
        EditText activity_register_password_repeat;
        @InjectBinder(method = "done", listeners = {OnClick.class})
        Button activity_register_done;
        TextView activity_register_agreement;
    }

    @InjectAll
    Views mViews;

    private int mAgreementColor;

    @InjectInit
    private void init(){
        mAgreementColor = ContextCompat.getColor(this, R.color.agreement_text);
        SpannableString spanString = new SpannableString("同意滨海视窗用户协议");
        spanString.setSpan(mClickableSpan, 2, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mViews.activity_register_agreement.setText(spanString);
        mViews.activity_register_agreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void done(){
        String email = mViews.activity_register_email.getText().toString();
        String nickname = mViews.activity_register_nickname.getText().toString();
        String password = mViews.activity_register_password.getText().toString();
        UserPresenter.getInstance(this).register(nickname, email, password);
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
}
