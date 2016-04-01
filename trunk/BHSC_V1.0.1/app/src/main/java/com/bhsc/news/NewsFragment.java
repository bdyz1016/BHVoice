package com.bhsc.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bhsc.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.net.MyRetryPolicy;
import com.bhsc.net.MySingleton;
import com.bhsc.news.adapter.NewsAdapter;
import com.bhsc.news.model.Data_DB_News;
import com.bhsc.news.newsdetail.DetailActivity;
import com.bhsc.news.newsdetail.NewsActivity;
import com.bhsc.utils.L;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsFragment extends Fragment implements NewsManager.OnNewsListener{
    private final String TAG = NewsFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";

    private View mContentView;

    private ListView fragment_news_list;

    private TextView fragment_news_default;

    private SwipeRefreshLayout fragment_news_refresh;

    private View mListFooter;

    private final int mNormalFooterHeight = 60;

    private TextView Tv_FooterText;
    private ProgressBar Pb_FooterProgress;

    private NewsAdapter mAdapter;

    private LayoutInflater inflater;

    private Context mContext;

    /**
     * 新闻类型，只加载符合类型的新闻
     */
    private int mNewsType;

    private LayoutInflater mInflater;

    private NewsManager mManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreateView");
        this.inflater = inflater;
        mContentView = inflater.inflate(R.layout.fragment_news, container, false);
        initWidget();
        initData();
        initView();
        return mContentView;
    }

    @Override
    public void onResume() {
        L.i(TAG, "onResume");
        super.onResume();
        mManager.refreshed(mNewsType);
    }

    @Override
    public void onPause() {
        super.onPause();
        mManager.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initWidget(){
        fragment_news_list = (ListView) mContentView.findViewById(R.id.fragment_news_list);
        fragment_news_default = (TextView) mContentView.findViewById(R.id.fragment_news_default);
        fragment_news_refresh = (SwipeRefreshLayout) mContentView.findViewById(R.id.fragment_news_refresh);
        fragment_news_list.setEmptyView(fragment_news_default);
        fragment_news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NewsActivity.INTENT_KEY_NEWSID, ((Data_DB_News)mAdapter.getItem(position)).getId());
                intent.setClass(mContext, NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        mManager = new NewsManager(mContext);
        mManager.setNewsListener(this);
        mInflater = LayoutInflater.from(mContext);
        mAdapter = new NewsAdapter(mContext);
    }

    private void initView(){
        mListFooter = mInflater.inflate(R.layout.footer_news_list, fragment_news_list, false);
        fragment_news_list.addFooterView(mListFooter);
        fragment_news_list.setAdapter(mAdapter);

        Tv_FooterText = (TextView) mListFooter.findViewById(R.id.newslist_footer_text);
        Pb_FooterProgress = (ProgressBar) mListFooter.findViewById(R.id.newslist_footer_progressbar);

        fragment_news_refresh.setOnRefreshListener(mRefreshListener);
        fragment_news_refresh.setColorSchemeResources(R.color.category_tab_highlight_bg);
    }

//    public void onEventMainThread(NewsEvent event) {
//        if(event.getNewsType() != mNewsType || event.getAction() != NewsEvent.ACTION_REFRESH_COMPLETE){//不是当前类型的新闻
//            return;
//        }
//        fragment_news_refresh.setRefreshing(false);
//        List<Data_DB_News> newsList = event.getNewsList();
//        int size = newsList.size();
//        if(size > 0) {
//            mNewsList.clear();
//            L.i(TAG, "初始化新闻列表:" + size);
//            L.i(TAG, "mNewsAdapter != null");
//            mNewsList.addAll(newsList);
//            mNewsAdapter.notifyDataSetChanged();
//        }
//        if(size < NewsPresenter.LOAD_NEWS_PAGE_SIZE){
//            Tv_FooterText.setText("没有更多新闻了");
//            Pb_FooterProgress.setVisibility(View.GONE);
//        }
//    }

    public void setNewsType(int type){
        L.i(TAG, "news type:" + type);
        mNewsType = type;
    }


    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
//            mPage = 1;
//            mNewsPresenter.getNews(mNewsType, mPage);
        }
    };

    @Override
    public void loaded(List<Data_DB_News> list) {
        mAdapter.addAll(list);
    }

    @Override
    public void refreshed(List<Data_DB_News> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }
}
