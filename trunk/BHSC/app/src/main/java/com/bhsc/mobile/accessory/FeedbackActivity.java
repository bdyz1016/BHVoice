package com.bhsc.mobile.accessory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.dataclass.Data_DB_FeedBack;
import com.bhsc.mobile.view.dialog.DefaultDialog;

import java.lang.ref.WeakReference;

/**
 * Created by lynn on 11/5/15.
 */
public class FeedbackActivity extends Activity {
    private final String TAG = "FeedBackActivity";

    public static final int MESSAGE_MAX_LENGTH = 200;

    public static final int UPLOAD_SUCCESS = 0;
    public static final int UPLOAD_FAILED = 1;

    private EditText Edtv_Content;
    private View Tv_Submit;
    private View Iv_GoBack;

    private ProgressDialog mProgressDialog;

    private DefaultDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        initWidget();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    private void initWidget(){
        Edtv_Content = (EditText) findViewById(R.id.activity_feedback_content);
        Tv_Submit = findViewById(R.id.activity_feedback_done);
        Iv_GoBack = findViewById(R.id.activity_feedback_back);
        Iv_GoBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        Tv_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideSoftInputMethod();
                String content = Edtv_Content.getText().toString();
                if (content.length() > MESSAGE_MAX_LENGTH) {
                    Toast.makeText(getApplicationContext(), "请不要超过200字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!content.equals("")) {
                    Data_DB_FeedBack feedback = new Data_DB_FeedBack();
                    feedback.setQuestion(content);
                    feedback.setSubmitTime(System.currentTimeMillis() / 1000);
                    new Thread(new UploadFeedback(feedback)).start();
                    showProgress();
                }
            }
        });
    }

    private class UploadFeedback implements Runnable{
        private Data_DB_FeedBack mFeedback;
        public UploadFeedback(Data_DB_FeedBack feedback){
            this.mFeedback = feedback;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message msg = mMyHandler.obtainMessage();

            if(true){
                msg.what = UPLOAD_SUCCESS;
            } else {
                msg.what = UPLOAD_FAILED;
            }
            mMyHandler.sendMessage(msg);
        }

    }

    private MyHandler mMyHandler = new MyHandler(FeedbackActivity.this);

    private static class MyHandler extends Handler {
        private final WeakReference<FeedbackActivity> activityRef;

        public MyHandler(FeedbackActivity activity){
            activityRef = new WeakReference<FeedbackActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            FeedbackActivity activity = activityRef.get();
            if(activity == null){
                return;
            }
            switch(msg.what){
                case UPLOAD_SUCCESS:
                    activity.clearEditText();
                    activity.dismissProgress();
                    activity.showDialog("您的使用意见已发送成功，感谢您的支持!");
                    break;
                case UPLOAD_FAILED:
                    activity.dismissProgress();
                    activity.showDialog("发送失败，请检查网络是否正常!");
                    break;
                default:
                    break;
            }
        }
    }

    public void clearEditText(){
        Edtv_Content.setText("");
    }

    public void dismissProgress(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public void showDialog(String message){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        mDialog = new DefaultDialog(this);
        mDialog.setMessage(message);
        mDialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        mDialog.show();
    }

    private void showProgress(){
        mProgressDialog = new ProgressDialog(FeedbackActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mProgressDialog.setMessage(getResources().getString(R.string.setting_user_proDia_message_upload));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Edtv_Content.getWindowToken(), 0);
    }

}
