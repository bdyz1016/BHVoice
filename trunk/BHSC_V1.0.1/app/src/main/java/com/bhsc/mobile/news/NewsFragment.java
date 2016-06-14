package com.bhsc.mobile.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.R;
import com.bhsc.mobile.news.adapter.NewsAdapter;
import com.bhsc.mobile.news.model.Data_DB_News;
import com.bhsc.mobile.news.newsdetail.NewsActivity;
import com.bhsc.mobile.utils.L;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsFragment extends Fragment implements NewsManager.OnNewsListener {
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

    private Context mContext;

    /**
     * 新闻类型，只加载符合类型的新闻
     */
    private int mNewsType;

    private boolean mIsInitialize = false;

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
        if (!mIsInitialize) {
            L.i(TAG, "刷新");
            fragment_news_refresh.setRefreshing(true);
            mManager.refreshed(mNewsType);
            mIsInitialize = true;
        } else {
            L.i(TAG, "不刷新");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mManager.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG, "onDestroy");
        mManager.cancel();
    }

    private void initWidget() {
        fragment_news_list = (ListView) mContentView.findViewById(R.id.fragment_news_list);
        fragment_news_default = (TextView) mContentView.findViewById(R.id.fragment_news_default);

        fragment_news_refresh = (SwipeRefreshLayout) mContentView.findViewById(R.id.fragment_news_refresh);
        fragment_news_refresh.setOnRefreshListener(mRefreshListener);
        fragment_news_refresh.setColorSchemeResources(R.color.category_tab_highlight_bg);

        fragment_news_list.setEmptyView(fragment_news_default);
        fragment_news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NewsActivity.INTENT_KEY_NEWSID, ((Data_DB_News) mAdapter.getItem(position)).getId());
                intent.setClass(mContext, NewsActivity.class);
                startActivity(intent);
            }
        });

        mListFooter = LayoutInflater.from(mContext).inflate(R.layout.footer_news_list, fragment_news_list, false);
        fragment_news_list.addFooterView(mListFooter);
        Tv_FooterText = (TextView) mListFooter.findViewById(R.id.newslist_footer_text);
        Pb_FooterProgress = (ProgressBar) mListFooter.findViewById(R.id.newslist_footer_progressbar);
    }

    private void initData() {
        mManager = new NewsManager(mContext);
        mManager.setNewsListener(this);
        mAdapter = new NewsAdapter(mContext);

        fragment_news_list.setAdapter(mAdapter);
        fragment_news_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能
                        mManager.loadFromCloud(mNewsType);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initView() {
        mManager.loadFromLocal(mNewsType);
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

    public void setNewsType(int type) {
        L.i(TAG, "news type:" + type);
        mNewsType = type;
    }


    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mManager.refreshed(mNewsType);
        }
    };

    @Override
    public void loaded(List<Data_DB_News> list) {
//        Collections.sort(list);
        mAdapter.addAll(list);
    }

    @Override
    public void refreshed(List<Data_DB_News> list) {
        fragment_news_refresh.setRefreshing(false);
        mAdapter.clear();
//        Collections.sort(list);
        mAdapter.addAll(list);
    }

    @Override
    public void error(int error) {
        fragment_news_refresh.setRefreshing(false);
        if (error == NewsManager.ERROR_LOAD_NO_MORE) {
            Tv_FooterText.setText(getString(R.string.no_more));
            Pb_FooterProgress.setVisibility(View.GONE);
        } else if(error == NewsManager.ERROR_REFRESH_NETWORK_UNREACHABLE){
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }
}
