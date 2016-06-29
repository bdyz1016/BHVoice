package com.bhsc.mobile.news.newsdetail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.ShareMenu;
import com.bhsc.mobile.comment.CommentActivity;
import com.bhsc.mobile.comment.CommentManager;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.net.RequestError;
import com.bhsc.mobile.news.model.Data_DB_Detail;
import com.bhsc.mobile.news.model.DetailResponse;
import com.bhsc.mobile.userpages.LoginAndRegisterActivity;
import com.bhsc.mobile.userpages.UserManager;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = NewsActivity.class.getSimpleName();
    public static final String INTENT_KEY_NEWSID = "newsId";
    public static final long DEFAULT_NEWSID = -1;

    private final String STATE_NEWS_ID = "news_id";
    private final String STATE_NEWS_DETAIL = "news_detail";

    private final String mimeType = "text/html; charset=UTF-8";
    private final String encoding = "UTF-8";

    private final String URL = MyApplication.Address + "/news/getNewsById";

    private WebView mWebVew;
    private EditText Edit_Discuss;
    private TextView Tv_DiscussCount;
    private View mContainer;
    private View mShare, mSend, mNormal;
    private ProgressBar mProgressBar;

    private Data_DB_Detail mNews;
    private CommentManager mManager;

    private ShareMenu mShareMenu;

    private Context mContext;
    private long mNewsId;

    private LoadNewsDetailTask mLoadNewsDetailTask;
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
        if(mNews == null) {
            loadNewsContent();
        } else {
            String content = mNews.getContent();
            content = content.replace("<img", "<img width=\"320\"");
            mWebVew.loadData(content, mimeType, null);
            Tv_DiscussCount.setText(mNews.getCommentCount() + "");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_NEWS_ID, mNewsId);
        outState.putParcelable(STATE_NEWS_DETAIL, mNews);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNewsId = savedInstanceState.getLong(STATE_NEWS_ID);
        mNews = savedInstanceState.getParcelable(STATE_NEWS_DETAIL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mShareMenu != null && mShareMenu.isShowing()){
                mShareMenu.dismiss();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                mShareMenu = new ShareMenu(mContext);
                ShareMenu.ShareContent shareContent = new ShareMenu.ShareContent(mNews.getTitle(), mNews.getTitle(), mNews.getTitleImg(), mNews.getShareurl());
                mShareMenu.show(mContainer, shareContent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.send:
                if (UserManager.isLogin()) {
                    mManager.addComment(UserManager.getUser().getId(), mNews.getId(),
                            Edit_Discuss.getText().toString(), CommentManager.TYPE_NEWS, null);
                } else {
                    startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
                }
                break;
            case R.id.discuss_count:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra(CommentActivity.INTENT_FATHER_ID, mNews.getId());
                intent.putExtra(CommentActivity.INTENT_COMMENT_TYPE, CommentManager.TYPE_NEWS);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initWidget() {
        mWebVew = (WebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        Edit_Discuss = (EditText) findViewById(R.id.discuss);
        Tv_DiscussCount = (TextView) findViewById(R.id.discuss_count);
        mContainer = findViewById(R.id.container);
        mShare = findViewById(R.id.share);
        mSend = findViewById(R.id.send);
        mNormal = findViewById(R.id.normal);
        findViewById(R.id.back).setOnClickListener(this);
        Edit_Discuss.setOnFocusChangeListener(mFocusChangeListener);
        mSend.setOnClickListener(this);
        mShare.setOnClickListener(this);
        Tv_DiscussCount.setOnClickListener(this);

        WebSettings webSettings = mWebVew.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultTextEncodingName(encoding);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    private void initData() {
        mContext = this;
        mNewsId = getIntent().getLongExtra(INTENT_KEY_NEWSID, DEFAULT_NEWSID);
        mManager = new CommentManager(this);

    }

    private void loadNewsContent(){
        mProgressBar.setVisibility(View.VISIBLE);
        mLoadNewsDetailTask = new LoadNewsDetailTask();
        mLoadNewsDetailTask.execute(mNewsId);
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
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
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
            if (hasFocus) {
                mNormal.setVisibility(View.GONE);
                mSend.setVisibility(View.VISIBLE);
            } else {
                mNormal.setVisibility(View.VISIBLE);
                mSend.setVisibility(View.GONE);
            }
        }
    };

    private class LoadNewsDetailTask extends AsyncTask<Long, Data_DB_Detail, Integer>{

        private final long REQUEST_TIME_OUT = 20 * 1000;

        private MyStringRequest mStringRequest;
        private Gson mGson;
        public LoadNewsDetailTask(){
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

        @Override
        protected Integer doInBackground(Long... params) {
            long newsId = params[0];
            if(!Method.isNetworkAvailable(mContext)){
                Data_DB_Detail detail = SugarRecord.findById(Data_DB_Detail.class, newsId);
                if(detail != null) {
                    publishProgress(detail);
                }
                return RequestError.ERROR_NETWORK_UNREACHABLE;
            } else {
                String url = URL + "?id=" + newsId;
                RequestFuture<String> requestFuture = RequestFuture.newFuture();
                mStringRequest = new MyStringRequest(Request.Method.GET, url, requestFuture, requestFuture);
                mStringRequest.setRetryPolicy(new MyRetryPolicy());
                MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
                try {
                    String response = requestFuture.get(REQUEST_TIME_OUT, TimeUnit.MILLISECONDS);
                    if(!TextUtils.isEmpty(response)){
                        L.i(TAG, "response:" + response);
                        DetailResponse detailResponse = mGson.fromJson(response, new TypeToken<DetailResponse>(){}.getType());
                        if(detailResponse != null){
                            if(detailResponse.getCode() == DetailResponse.SUCESS_CODE){
                                detailResponse.getNews().save();
                                publishProgress(detailResponse.getNews());
                            } else {
                                return RequestError.ERROR_UNKNOWN;
                            }
                        } else {
                            return RequestError.ERROR_SERVER_ISSUES;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    return RequestError.ERROR_TIME_OUT;
                }
            }
            return RequestError.ERROR_SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Data_DB_Detail... values) {
            super.onProgressUpdate(values);
            mNews = values[0];
            String content = mNews.getContent();
            content = content.replace("<img", "<img width=\"320\"");
            mWebVew.loadData(content, mimeType, null);
            Tv_DiscussCount.setText(mNews.getCommentCount() + "");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mProgressBar.setVisibility(View.GONE);
            switch (integer) {
                case RequestError.ERROR_NETWORK_UNREACHABLE:
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case RequestError.ERROR_SERVER_ISSUES:
                    Toast.makeText(mContext, "服务器错误", Toast.LENGTH_SHORT).show();
                    break;
                case RequestError.ERROR_UNKNOWN:
                    Toast.makeText(mContext, "位置异常", Toast.LENGTH_SHORT).show();
                    break;
                case RequestError.ERROR_TIME_OUT:
                    Toast.makeText(mContext, "请求超时", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(mStringRequest!= null && !mStringRequest.isCanceled()){
                mStringRequest.cancel();
            }
        }
    }
}
