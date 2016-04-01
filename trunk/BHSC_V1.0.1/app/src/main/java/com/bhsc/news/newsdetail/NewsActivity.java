package com.bhsc.news.newsdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.bhsc.mobile.R;
import com.bhsc.news.model.Data_DB_News;
import com.orm.SugarRecord;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String INTENT_KEY_NEWSID = "newsId";
    public static final long DEFAULT_NEWSID = -1;
    private final String mimeType = "text/html";
    private final String encoding = "utf-8";
    private WebView mWebVew;
    private Data_DB_News mNews;

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
            default:
                break;
        }
    }

    private void initWidget(){
        mWebVew = (WebView) findViewById(R.id.webview);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void initData(){
        long id = getIntent().getLongExtra(INTENT_KEY_NEWSID, DEFAULT_NEWSID);
        if(id > 0){
            mNews = SugarRecord.findById(Data_DB_News.class, id);
        }
    }

    private void initView(){
        if(mNews != null){
            mWebVew.loadData(mNews.getContent(), mimeType, encoding);
        }
    }
}
