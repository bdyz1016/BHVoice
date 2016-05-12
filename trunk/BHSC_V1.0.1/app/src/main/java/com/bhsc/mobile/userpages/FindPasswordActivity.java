package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.net.ObjectResponse;
import com.bhsc.mobile.userpages.dialog.DefaultDialog;
import com.bhsc.mobile.utils.Method;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 16/5/11.
 */
public class FindPasswordActivity extends Activity {

    private EditText Edit_Email;
    private EditText Edit_VerifyCode;
    private EditText Edit_Password;
    private EditText Edit_RepeatPassword;
    private Button Btn_SendVerifyCode;
    private Button Btn_Confirm;

    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    private void initWidget(){
        Edit_Email = (EditText) findViewById(R.id.email);
        Edit_VerifyCode = (EditText) findViewById(R.id.verify_code);
        Edit_Password = (EditText) findViewById(R.id.password);
        Edit_RepeatPassword = (EditText) findViewById(R.id.password_repeat);
        Btn_SendVerifyCode = (Button) findViewById(R.id.send_verify_code);
        Btn_SendVerifyCode.setOnClickListener(mSendVerifyCodeClickListener);
        Btn_Confirm = (Button) findViewById(R.id.done);
        Btn_Confirm.setOnClickListener(mChangePasswordClickListener);
    }

    private void initData(){
        mContext = this;
    }

    private View.OnClickListener mSendVerifyCodeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = Edit_Email.getText().toString();
            if(!Method.checkEmail(email)){
                Toast.makeText(mContext, "邮箱格式错误!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Method.isNetworkAvailable(mContext)){
                Toast.makeText(mContext, "网络不可用!", Toast.LENGTH_SHORT).show();
                return;
            }
            displayProgressDialog("");
            String url = MyApplication.Address + "/user/sendCode?email=" + email;
            MyStringRequest request = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dismissProgressDialog();
                    if(TextUtils.isEmpty(response)){
                        Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Gson gson = new Gson();
                    ObjectResponse objectResponse = gson.fromJson(response, new TypeToken<ObjectResponse>(){}.getType());
                    if(objectResponse.getCode() == ObjectResponse.SUCESS_CODE){
                        Toast.makeText(mContext, "发送成功，请输入验证码!", Toast.LENGTH_SHORT).show();
                        Btn_SendVerifyCode.setClickable(false);
                        new CountdownTask().execute();
                    } else if(objectResponse.getCode() == 201){
                        Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
                    } else if(objectResponse.getCode() == 202){
                        Toast.makeText(mContext, "系统异常!", Toast.LENGTH_SHORT).show();
                    } else if(objectResponse.getCode() == 203){
                        Toast.makeText(mContext, "用户不存在!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissProgressDialog();
                    Toast.makeText(mContext, "发送失败!", Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(FindPasswordActivity.this).addToRequestQueue(request);
        }
    };

    private View.OnClickListener mChangePasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String email = Edit_Email.getText().toString();
            final String verifyCode = Edit_VerifyCode.getText().toString();
            final String password = Edit_Password.getText().toString();
            String repeatPassword = Edit_RepeatPassword.getText().toString();
            if(!Method.checkEmail(email)){
                Toast.makeText(mContext, "邮箱格式错误!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(verifyCode)){
                Toast.makeText(mContext, "验证码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Method.checkPassword(password)){
                Toast.makeText(mContext, "密码格式错误!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(repeatPassword)){
                Toast.makeText(mContext, "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Method.isNetworkAvailable(mContext)){
                Toast.makeText(mContext, "网络不可用!", Toast.LENGTH_SHORT).show();
                return;
            }
            displayProgressDialog("");
            String url = MyApplication.Address + "/user/findpwd";
            MyStringRequest request = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dismissProgressDialog();
                    if(TextUtils.isEmpty(response)){
                        Toast.makeText(mContext, "重置密码失败!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Gson gson = new Gson();
                    ObjectResponse objectResponse = gson.fromJson(response, new TypeToken<ObjectResponse>(){}.getType());
                    if(objectResponse.getCode() == ObjectResponse.SUCESS_CODE){
                        final DefaultDialog dialog = new DefaultDialog(mContext);
                        dialog.setMessage("密码修改成功，请登录!");
                        dialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.setOnNegativeClickListener(new DefaultDialog.OnButtonClickListener() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else if(objectResponse.getCode() == 201){
                        Toast.makeText(mContext, "系统异常，重置密码失败!", Toast.LENGTH_SHORT).show();
                    } else if(objectResponse.getCode() == 202){
                        Toast.makeText(mContext, "验证码错误!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "重置密码失败!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("code", verifyCode);
                    params.put("password", password);
                    return params;
                }
            };
            MySingleton.getInstance(mContext).addToRequestQueue(request);
        }
    };

    private class CountdownTask extends AsyncTask<Void, Integer, Integer>{

        public final int TOTAL_COUNT = 120;

        @Override
        protected Integer doInBackground(Void... params) {
            int count = TOTAL_COUNT;
            try {
                while (count >= 0) {
                    publishProgress(count);
                    count--;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Btn_SendVerifyCode.setText(values[0] + "");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Btn_SendVerifyCode.setText(getResources().getString(R.string.send_verify_code));
            Btn_SendVerifyCode.setClickable(true);
        }
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
