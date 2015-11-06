package com.bhsc.mobile.homepage;

import android.app.Activity;
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

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.image.ImageDownloader;
import com.android.pc.ioc.image.Utils;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.dataclass.Data_DB_News;
import com.bhsc.mobile.homepage.adapter.NewsAdapter;
import com.bhsc.mobile.homepage.event.NewsEvent;
import com.bhsc.mobile.homepage.newsdetail.DetailActivity;
import com.bhsc.mobile.homepage.newsdetail.DiscussEvent;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.SyncArrayList;
import com.bhsc.mobile.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsFragment extends Fragment {
    private final String TAG = NewsFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";

    @InjectView(binders = {@InjectBinder(method = "onItemClick", listeners = {OnItemClick.class})})
    ListView fragment_news_list;

    @InjectView
    TextView fragment_news_default;

    @InjectView
    RefreshLayout fragment_news_refresh;

    private View mListFooter;

    private final int mNormalFooterHeight = 60;

    private TextView Tv_FooterText;
    private ProgressBar Pb_FooterProgress;

    private LayoutInflater inflater;

    private final SyncArrayList<Data_DB_News> mNewsList = new SyncArrayList<>(new ArrayList<Data_DB_News>());
    private NewsAdapter mNewsAdapter;

    private Context mContext;

    public static ImageDownloader mImageFetcher = null;

    /**
     * 新闻类型，只加载符合类型的新闻
     */
    private int mNewsType;

    /**
     * 当前加载的页面
     */
    private int mPage;

    private NewsPresenter mNewsPresenter;

    private LayoutInflater mInflater;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreateView");
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        Handler_Inject.injectFragment(this, view);
        return view;
    }

    @Override
    public void onResume() {
        L.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void init() {
        L.i(TAG, "init");
        initData();
        initView();
    }

    private void initData(){
        /**
         * 加载新闻
         */
        mPage = 1;
        mNewsPresenter = NewsPresenter.getInstance(mContext);
        mInflater = LayoutInflater.from(mContext);
        mImageFetcher = new ImageDownloader(getActivity(), 300);
        mImageFetcher.setLoadingImage(R.mipmap.icon_no_image);
        mNewsAdapter = new NewsAdapter(mContext, mNewsList);
    }

    private void initView(){
        mListFooter = mInflater.inflate(R.layout.footer_news_list, fragment_news_list, false);
        fragment_news_list.setOnScrollListener(mScrollListener);
        fragment_news_list.setEmptyView(fragment_news_default);
        fragment_news_list.addFooterView(mListFooter);
        fragment_news_list.setAdapter(mNewsAdapter);

        Tv_FooterText = (TextView) mListFooter.findViewById(R.id.newslist_footer_text);
        Pb_FooterProgress = (ProgressBar) mListFooter.findViewById(R.id.newslist_footer_progressbar);

        fragment_news_refresh.setOnRefreshListener(mRefreshListener);
        fragment_news_refresh.setColorSchemeResources(R.color.category_tab_highlight_bg);

        mNewsPresenter.getNews(mNewsType, mPage);
    }

    public void onEventMainThread(NewsEvent event) {
        if(event.getNewsType() != mNewsType || event.getAction() != NewsEvent.ACTION_REFRESH_COMPLETE){//不是当前类型的新闻
            return;
        }
        fragment_news_refresh.setRefreshing(false);
        List<Data_DB_News> newsList = event.getNewsList();
        int size = newsList.size();
        if(size > 0) {
            mNewsList.clear();
            L.i(TAG, "初始化新闻列表:" + size);
            L.i(TAG, "mNewsAdapter != null");
            mNewsList.addAll(newsList);
            mNewsAdapter.notifyDataSetChanged();
        }
        if(size < NewsPresenter.LOAD_NEWS_PAGE_SIZE){
            Tv_FooterText.setText("没有更多新闻了");
            Pb_FooterProgress.setVisibility(View.GONE);
        }
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position >= mNewsList.size()){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.INTENT_KEY_NEWSID, mNewsList.get(position).getId());
        intent.setClass(mContext, DetailActivity.class);
        startActivity(intent);
    }

    private void loadMore(){}

    public void setNewsType(int type){
        L.i(TAG, "news type:" + type);
        mNewsType = type;
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                // Before Honeycomb pause image loading on scroll to help with performance
                if (!Utils.hasHoneycomb()) {
                    mImageFetcher.setPauseWork(true);
                }
            } else {
                mImageFetcher.setPauseWork(false);
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                // 判断是否滚动到底部
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    //加载更多
                    Tv_FooterText.setText(getString(R.string.loading));
                    Pb_FooterProgress.setVisibility(View.VISIBLE);
                    mPage++;
                    mNewsPresenter.getNews(mNewsType, mPage);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            L.i(TAG, "firstVisibleItem:" + firstVisibleItem + ",visibleItemCount:" + visibleItemCount);
            int topRowVerticalPosition = (fragment_news_list == null || fragment_news_list.getChildCount() == 0) ?
                            0 : fragment_news_list.getChildAt(0).getTop();
            if(firstVisibleItem == 0 && topRowVerticalPosition >= 0){
                fragment_news_refresh.setEnabled(true);
            } else {
                fragment_news_refresh.setEnabled(false);
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPage = 1;
            mNewsPresenter.getNews(mNewsType, mPage);
        }
    };
}
