package com.bhsc.mobile.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.userpages.dialog.DefaultDialog;

/**
 * Created by zhanglei on 16/5/11.
 */
public class FeedbackActivity extends Activity {

    private TextView Tv_Title;
    private Button Btn_Done;
    private EditText Edit_Content;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Edit_Content = (EditText) findViewById(R.id.content);
        Btn_Done = (Button) findViewById(R.id.done);
        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = Edit_Content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(FeedbackActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayProgressDialog("");
                new AsyncTask<String, Integer, Integer>(){
                    @Override
                    protected Integer doInBackground(String... params) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }

                    @Override
                    protected void onPostExecute(Integer integer) {
                        dismissProgressDialog();
                        DefaultDialog dialog = new DefaultDialog(FeedbackActivity.this);
                        dialog.setMessage("发送成功，感谢您的支持!");
                        dialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
                            @Override
                            public void onClick() {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }.execute("");
            }
        });
        Tv_Title = (TextView) findViewById(R.id.title);
        Tv_Title.setText(R.string.title_feedback);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
