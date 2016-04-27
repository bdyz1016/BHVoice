package com.bhsc.mobile.news.newsdetail;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.comment.CommentManager;
import com.bhsc.mobile.news.model.Data_DB_News;
import com.orm.SugarRecord;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String INTENT_KEY_NEWSID = "newsId";
    public static final long DEFAULT_NEWSID = -1;
    private final String mimeType = "text/html";
    private final String encoding = "utf-8";
    private WebView mWebVew;
    private EditText Edit_Discuss;
    private TextView Tv_DiscussCount;
    private View mShare, mSend, mNormal;
    private Data_DB_News mNews;
    private CommentManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                mManager.addComment(mNews.getId(), Edit_Discuss.getText().toString(), CommentManager.TYPE_NEWS);
                break;
            case R.id.discuss_count:
                break;
            default:
                break;
        }
    }

    private void initWidget(){
        mWebVew = (WebView) findViewById(R.id.webview);
        Edit_Discuss = (EditText) findViewById(R.id.discuss);
        Tv_DiscussCount = (TextView) findViewById(R.id.discuss_count);
        mShare = findViewById(R.id.share);
        mSend = findViewById(R.id.send);
        mNormal = findViewById(R.id.normal);
        findViewById(R.id.back).setOnClickListener(this);
        Edit_Discuss.setOnFocusChangeListener(mFocusChangeListener);
    }

    private void initData(){
        long id = getIntent().getLongExtra(INTENT_KEY_NEWSID, DEFAULT_NEWSID);
        if(id > 0){
            mNews = SugarRecord.findById(Data_DB_News.class, id);
        }
        mManager = new CommentManager(this);
    }

    private void initView(){
        if(mNews != null){
            mWebVew.loadData(mNews.getContent(), mimeType, encoding);
            Tv_DiscussCount.setText(mNews.getCommentCount() + "");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {//触摸屏幕隐藏软键盘
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 判断点击位置是否应该隐藏软键盘
     * @param v
     * @param event
     * @return
     */
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                mNormal.setVisibility(View.GONE);
                mSend.setVisibility(View.VISIBLE);
            } else {
                mNormal.setVisibility(View.VISIBLE);
                mSend.setVisibility(View.GONE);
            }
        }
    };
}
