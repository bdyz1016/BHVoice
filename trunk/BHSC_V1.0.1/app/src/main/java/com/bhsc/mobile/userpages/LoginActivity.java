package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.bhsc.mobile.userpages.model.UserResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lynn on 10/14/15.
 */
public class LoginActivity extends Activity {
    private final String TAG = LoginActivity.class.getSimpleName();

    private View activity_login_back;
    private View activity_login_register;
    private EditText activity_login_username;
    private EditText activity_login_password;
    private TextView activity_login_warning;
    private View activity_login_forget_password;
    private Button activity_login_login;

    private TextView login_error_1;

    private LayoutInflater mInflater;

    private StringRequest mRequest;

    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        activity_login_back = findViewById(R.id.activity_login_back);
        activity_login_register = findViewById(R.id.activity_login_register);
        activity_login_username = (EditText) findViewById(R.id.activity_login_username);
        activity_login_password = (EditText) findViewById(R.id.activity_login_password);
        activity_login_warning = (TextView) findViewById(R.id.activity_login_warning);
        activity_login_forget_password = findViewById(R.id.activity_login_forget_password);
        activity_login_login = (Button) findViewById(R.id.activity_login_login);

        activity_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        activity_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        activity_login_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
        activity_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void init() {
        mInflater = LayoutInflater.from(LoginActivity.this);
        activity_login_username.addTextChangedListener(mEmailWatcher);
        activity_login_password.addTextChangedListener(mPasswdWatcher);

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

    private void login() {
        hideSoftInputMethod();

        String userName = activity_login_username.getText().toString();
        String password = activity_login_password.getText().toString();
        login(userName, password);
    }

    private void forgetPassword() {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    private void goBack() {
        finish();
    }

    private void displayLoginFailedDialog() {
        View view = mInflater.inflate(R.layout.dialog_loginfailed, null);
        Toast toast = new Toast(LoginActivity.this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    public void onEventMainThread(UserEvent event){
//        if(event.getAction() == UserEvent.ACTION_LOGIN_SUCCESS){
//            L.i(TAG,"login success!");
//            finish();
//        } else if(event.getAction() == UserEvent.ACTION_LOGIN_FAILED){
//            L.i(TAG, "login failed!");
//            switch (event.getErrorCode()){
//                case UserEvent.ERROR_USERNAME_EMPTY:
//                    break;
//                case UserEvent.ERROR_USERNAME_FORMAT:
//                    mViews.activity_login_username.setText("");
//                    break;
//                case UserEvent.ERROR_USERNAME_WRONG:
//                    mViews.activity_login_password.setText("");
//                    mViews.activity_login_username.setText("");
//                    break;
//                case UserEvent.ERROR_PASSWORD_EMPTY:
//                    break;
//                case UserEvent.ERROR_PASSWORD_FORMAT:
//                    mViews.activity_login_password.setText("");
//                    break;
//                case UserEvent.ERROR_PASSWORD_WRONG:
//                    mViews.activity_login_password.setText("");
//                    break;
//            }
//            String warningText;
//            try {
//                warningText = (String) event.getExtra();
//            } catch (ClassCastException e){
//                e.printStackTrace();
//                warningText = this.getString(R.string.login_error_2);
//            }
//            if(!TextUtils.isEmpty(warningText)) {
//                mViews.activity_login_warning.setText(warningText);
//                shake(mViews.activity_login_warning);
//            }
//            displayLoginFailedDialog();
//        }
//    }

    private void shake(View v) {
        v.clearAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        v.startAnimation(shake);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity_login_password.getWindowToken(), 0);
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

    public void login(final String username, final String password) {
        int checkFormatResult = Method.checkUserNameAndPswd(username, password);

        //检验用户名密码格式是否正确
        if (checkFormatResult != 0) {
            String message;
            switch (checkFormatResult) {
                case UserManager.ERROR_USERNAME_EMPTY:
                    message = getString(R.string.login_error_5);
                    break;
                case UserManager.ERROR_USERNAME_FORMAT:
                    message = getString(R.string.login_error_6);
                    activity_login_username.setText("");
                    break;
                case UserManager.ERROR_PASSWORD_EMPTY:
                    message = getString(R.string.login_error_7);
                    break;
                case UserManager.ERROR_PASSWORD_FORMAT:
                    message = getString(R.string.login_error_8);
                    activity_login_password.setText("");
                    break;
                default:
                    message = getString(R.string.login_error_2);
                    break;
            }
            activity_login_warning.setText(message);
            shake(activity_login_warning);
            displayLoginFailedDialog();
            return;
        }
        loginCancel();
        mRequest = new StringRequest(Request.Method.POST, MyApplication.Address + "/user/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    Data_DB_User user = userResponse.getUser();
                    user.setPassword(password);
                    UserManager.login(user);
                    L.i(TAG,"login success!");
                    finish();
                } else if (userResponse.getCode() == 201) {
                    activity_login_username.setText("");
                    activity_login_password.setText("");
                    activity_login_warning.setText(getString(R.string.login_error_0));
                    shake(activity_login_warning);
                    displayLoginFailedDialog();
                } else if (userResponse.getCode() == 202) {
                    activity_login_password.setText("");
                    activity_login_warning.setText(getString(R.string.login_error_1));
                    shake(activity_login_warning);
                    displayLoginFailedDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity_login_username.setText("");
                activity_login_password.setText("");
                activity_login_warning.setText(getString(R.string.login_error_2));
                shake(activity_login_warning);
                displayLoginFailedDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("loginId", username);
                params.put("password", password);
                return params;
            }
        };
        mRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(this).getRequestQueue().add(mRequest);
    }

    public void loginCancel(){
        if(mRequest != null && !mRequest.isCanceled()){
            mRequest.cancel();
        }
    }
}
