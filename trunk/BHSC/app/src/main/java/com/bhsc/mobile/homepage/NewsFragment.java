package com.bhsc.mobile.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.image.ImageDownloader;
import com.android.pc.ioc.image.Utils;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectPullRefresh;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.PullToRefreshManager;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.homepage.adapter.NewsAdapter;
import com.bhsc.mobile.homepage.event.NewsEvent;
import com.bhsc.mobile.homepage.newsdetail.DetailActivity;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.SyncArrayList;

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

    private LayoutInflater inflater;

    private final SyncArrayList<Data_DB_News> mNewsList = new SyncArrayList<>(new ArrayList<Data_DB_News>());
    private NewsAdapter mNewsAdapter;

    private Context mContext;

    public static ImageDownloader mImageFetcher = null;

    /**
     * 新闻类型，只加载符合类型的新闻
     */
    private int mNewsType;

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
        mInflater = LayoutInflater.from(mContext);

        mImageFetcher = new ImageDownloader(getActivity(), 300);
        mImageFetcher.setLoadingImage(R.mipmap.icon_no_image);

        mNewsAdapter = new NewsAdapter(mContext, mNewsList);
        fragment_news_list.setOnScrollListener(mScrollListener);
        fragment_news_list.setEmptyView(fragment_news_default);
        fragment_news_list.addFooterView(mInflater.inflate(R.layout.footer_news_list, fragment_news_list, false));
        fragment_news_list.setAdapter(mNewsAdapter);
    }

    public void onEventMainThread(NewsEvent event) {
        if(event.getNewsType() != mNewsType || event.getAction() != NewsEvent.ACTION_REFRESH_COMPLETE){//不是当前类型的新闻
            return;
        }
        List<Data_DB_News> newsList = event.getNewsList();
        L.i(TAG, "初始化新闻列表:" + newsList.size());
        mNewsList.addAll(newsList);
        L.i(TAG, "mNewsAdapter != null");
        mNewsList.addAll(newsList);
        mNewsAdapter.notifyDataSetChanged();
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position >= mNewsList.size()){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, DetailActivity.class);
        startActivity(intent);
    }

    private void loadMore(){}

    public void setNewsType(int type){
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
                    Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            L.i(TAG, "firstVisibleItem:" + firstVisibleItem + ",visibleItemCount:" + visibleItemCount);
            int topRowVerticalPosition = (fragment_news_list == null || fragment_news_list.getChildCount() == 0) ?
                            0 : fragment_news_list.getChildAt(0).getTop();
            if(firstVisibleItem == 0 && topRowVerticalPosition >= 0){
                NewsEvent event = new NewsEvent();
                event.setAction(NewsEvent.ACTION_REFRESH_ENABLE);
                event.setNewsType(mNewsType);
                EventBus.getDefault().post(event);
            } else {
                NewsEvent event = new NewsEvent();
                event.setAction(NewsEvent.ACTION_REFRESH_DISABLE);
                event.setNewsType(mNewsType);
                EventBus.getDefault().post(event);
            }
        }
    };
}
