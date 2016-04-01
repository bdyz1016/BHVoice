package com.bhsc.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bhsc.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.net.MyRetryPolicy;
import com.bhsc.net.MySingleton;
import com.bhsc.userpages.model.UserResponse;
import com.bhsc.utils.L;
import com.bhsc.utils.Method;
import com.bhsc.views.BHRatioButton;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lynn on 10/15/15.
 */
public class RegisterActivity extends Activity {
    private final String TAG = RegisterActivity.class.getSimpleName();

    private View activity_register_back;
    private EditText activity_register_email;
    private EditText activity_register_password;
    private EditText activity_register_password_repeat;
    private TextView activity_register_warning;
    private Button activity_register_done;
    private TextView activity_register_agreement;
    private BHRatioButton login_agreement;

    private LayoutInflater mInflater;

    private int mAgreementColor;

    /**
     * 是否同意滨海视窗用户协议
     */
    private boolean mAgreement = false;

    private StringRequest mRequest;

    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initWidget() {
        activity_register_back = findViewById(R.id.activity_register_back);
        activity_register_email = (EditText) findViewById(R.id.activity_register_email);
        activity_register_password = (EditText) findViewById(R.id.activity_register_password);
        activity_register_password_repeat = (EditText) findViewById(R.id.activity_register_password_repeat);
        activity_register_warning = (TextView) findViewById(R.id.activity_register_warning);
        activity_register_done = (Button) findViewById(R.id.activity_register_done);
        activity_register_agreement = (TextView) findViewById(R.id.activity_register_agreement);
        login_agreement = (BHRatioButton) findViewById(R.id.login_agreement);
        activity_register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activity_register_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        activity_register_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    private void init() {
        mInflater = LayoutInflater.from(this);

        mAgreementColor = ContextCompat.getColor(this, R.color.agreement_text);
        SpannableString spanString = new SpannableString("同意滨海视窗用户协议");
        spanString.setSpan(mClickableSpan, 2, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        activity_register_agreement.setText(spanString);
        activity_register_agreement.setMovementMethod(LinkMovementMethod.getInstance());

        login_agreement.setOnCheckedChangeListener(new BHRatioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                mAgreement = isChecked;
            }
        });

        activity_register_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_agreement.setIsChecked(!login_agreement.isChecked());
            }
        });

        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz == Field.class || clazz == Request.Method.class;
            }
        };

        mGson = new GsonBuilder()
                .addSerializationExclusionStrategy(exclusionStrategy)
                .addDeserializationExclusionStrategy(exclusionStrategy)
                .create();
    }

    private void done() {
        hideSoftInputMethod();

        String email = activity_register_email.getText().toString();
        String password = activity_register_password.getText().toString();
        String confirmPassword = activity_register_password_repeat.getText().toString();
        register(email, password, confirmPassword);
    }

    private void goBack() {
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

    private void displayLoginFailedDialog() {
        View view = mInflater.inflate(R.layout.dialog_register_failed, null);
        Toast toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void shake(View v) {
        v.clearAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        v.startAnimation(shake);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity_register_password_repeat.getWindowToken(), 0);
    }

    private void register(final String username, final String password, String confirmPassword) {
        //检验两次密码是否一致
        if (TextUtils.isEmpty(password)) {
            activity_register_warning.setText(getString(R.string.login_error_7));
            shake(activity_register_warning);
            displayLoginFailedDialog();
            return;
        } else if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            activity_register_password.setText("");
            activity_register_password_repeat.setText("");
            activity_register_warning.setText(getString(R.string.register_error_0));
            shake(activity_register_warning);
            displayLoginFailedDialog();
            return;
        }

        int checkFormatResult = Method.checkUserNameAndPswd(username, password);
        //检验用户名密码格式是否正确
        if (checkFormatResult != 0) {
            String message;
            switch (checkFormatResult) {
                case UserManager.ERROR_USERNAME_EMPTY:
                    message = getString(R.string.login_error_5);
                    break;
                case UserManager.ERROR_USERNAME_FORMAT:
                    activity_register_email.setText("");
                    message = getString(R.string.login_error_6);
                    break;
                case UserManager.ERROR_PASSWORD_EMPTY:
                    message = getString(R.string.login_error_7);
                    break;
                case UserManager.ERROR_PASSWORD_FORMAT:
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    message = getString(R.string.login_error_8);
                    break;
                case UserManager.ERROR_USERNAME_EXIST:
                    activity_register_email.setText("");
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    message = getString(R.string.login_error_3);
                    break;
                case UserManager.ERROR_PASSWORD_INCONSISTENT:
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    message = getString(R.string.register_error_0);
                    break;
                default:
                    activity_register_email.setText("");
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    message = getString(R.string.login_error_2);
                    break;
            }
            activity_register_warning.setText(message);
            shake(activity_register_warning);
            return;
        }
        registerCancel();
        mRequest = new StringRequest(Request.Method.POST, MyApplication.Address + "/user/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    UserManager.login(userResponse.getUser());
                    L.i(TAG,"login success!");
                    finish();
                } else if (userResponse.getCode() == 201) {
                    activity_register_email.setText("");
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    activity_register_warning.setText(getString(R.string.login_error_4));
                    shake(activity_register_warning);
                    displayLoginFailedDialog();
                } else if (userResponse.getCode() == 202) {
                    activity_register_email.setText("");
                    activity_register_password.setText("");
                    activity_register_password_repeat.setText("");
                    activity_register_warning.setText(getString(R.string.login_error_3));
                    shake(activity_register_warning);
                    displayLoginFailedDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity_register_email.setText("");
                activity_register_password.setText("");
                activity_register_password_repeat.setText("");
                activity_register_warning.setText(getString(R.string.login_error_2));
                shake(activity_register_warning);
                displayLoginFailedDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("loginId", username);
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };
        mRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(this).getRequestQueue().add(mRequest);
    }

    public void registerCancel(){
        if(mRequest != null && !mRequest.isCanceled()){
            mRequest.cancel();
        }
    }
}
