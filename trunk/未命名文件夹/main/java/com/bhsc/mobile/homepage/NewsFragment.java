package com.bhsc.mobile.homepage;

import android.app.Activity;
import android.content.Context;
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
import com.android.pc.ioc.view.listener.OnItemClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.homepage.adapter.NewsAdapter;
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

    @InjectView(down = false, binders = {@InjectBinder(method = "onItemClick", listeners = {OnItemClick.class})})
    ListView fragment_news_list;

    @InjectView
    TextView fragment_news_default;

    /**
     * 是否已经加载新闻
     */
    private boolean mNewsIsLoaded = false;

    private LayoutInflater inflater;

    private final SyncArrayList<Data_DB_News> mNewsList = new SyncArrayList<>(new ArrayList<Data_DB_News>());
    private NewsAdapter mNewsAdapter;

    private Context mContext;

    public static ImageDownloader mImageFetcher = null;

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
        if (!mNewsIsLoaded) {
            NewsPresenter.getInstance().getNews();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void init() {
        L.i(TAG, "init");
        mNewsIsLoaded = false;

        mImageFetcher = new ImageDownloader(getActivity(), 300);
        mImageFetcher.setLoadingImage(R.drawable.temp);

        mNewsAdapter = new NewsAdapter(mContext, mNewsList);
        fragment_news_list.setAdapter(mNewsAdapter);
        fragment_news_list.setOnScrollListener(mScrollListener);

        PullToRefreshManager.getInstance().setRelease_label("松开后刷新");
    }

    public void onEventMainThread(List<Data_DB_News> newsList) {
        L.i(TAG, "初始化新闻列表:" + newsList.size());
        fragment_news_default.setVisibility(View.GONE);
        fragment_news_list.setVisibility(View.VISIBLE);
        mNewsList.addAll(newsList);
        L.i(TAG, "mNewsAdapter != null");
        mNewsList.addAll(newsList);
        mNewsAdapter.notifyDataSetChanged();
    }

    @InjectPullRefresh
    private void refreshCallBack(int type) {
        switch (type) {
            case InjectView.PULL:
                break;
            case InjectView.DOWN:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        PullToRefreshManager.getInstance().onHeaderRefreshComplete();
                    }
                }).start();
                break;
        }
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext, "Item Clicked:" + position, Toast.LENGTH_LONG).show();
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
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };
}
