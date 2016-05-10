package com.bhsc.mobile.news;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.news.adapter.NewsAdapter;
import com.bhsc.mobile.news.model.Data_DB_News;

import java.util.List;

/**
 * Created by lynn on 15-10-8.
 */
public class SearchActivity extends Activity implements NewsManager.OnSearchListener{

    private ListView Lv_Search;
    private EditText Edit_Search;
    private View mEmptyView_1, mEmptyView_2, mEmptyView_3;

    private NewsAdapter mAdapter;
    private NewsManager mNewsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
        initView();
    }

    private void initWidget(){
        Lv_Search = (ListView) findViewById(R.id.search_list);
        Edit_Search = (EditText) findViewById(R.id.search_box);

        mEmptyView_1 = findViewById(R.id.empty_view_1);
        mEmptyView_2 = findViewById(R.id.empty_view_2);
        mEmptyView_3 = findViewById(R.id.empty_view_3);
    }

    private void initData(){
        mNewsManager = new NewsManager(this);
        mAdapter = new NewsAdapter(this);
        Lv_Search.setAdapter(mAdapter);
        Edit_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = Edit_Search.getText().toString();
                    if (TextUtils.isEmpty(keyword)) {
                        mEmptyView_1.setVisibility(View.VISIBLE);
                        mEmptyView_2.setVisibility(View.GONE);
                        mEmptyView_3.setVisibility(View.GONE);
                        Lv_Search.setEmptyView(mEmptyView_1);
                    } else {
                        mEmptyView_1.setVisibility(View.GONE);
                        mEmptyView_2.setVisibility(View.VISIBLE);
                        mEmptyView_3.setVisibility(View.GONE);
                        Lv_Search.setEmptyView(mEmptyView_2);
                        mNewsManager.search(keyword, SearchActivity.this);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initView(){
        mEmptyView_1.setVisibility(View.VISIBLE);
        Lv_Search.setEmptyView(mEmptyView_1);
    }

    @Override
    public void result(List<Data_DB_News> list) {
        mAdapter.clear();
        if(list != null && list.size() > 0) {
            mAdapter.addAll(list);
        } else {
            mEmptyView_1.setVisibility(View.GONE);
            mEmptyView_2.setVisibility(View.GONE);
            mEmptyView_3.setVisibility(View.VISIBLE);
            Lv_Search.setEmptyView(mEmptyView_3);
        }
    }

    @Override
    public void error() {
        mAdapter.clear();
        mEmptyView_1.setVisibility(View.VISIBLE);
        mEmptyView_2.setVisibility(View.GONE);
        mEmptyView_3.setVisibility(View.GONE);
        Lv_Search.setEmptyView(mEmptyView_1);
    }

    public void cancel(View view) {
        finish();
    }
}
