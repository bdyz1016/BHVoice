package com.bhsc.mobile.news;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.R;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.news.adapter.NewsAdapter;
import com.bhsc.mobile.news.model.Data_DB_News;
import com.bhsc.mobile.news.model.NewsResponse;
import com.bhsc.mobile.news.newsdetail.NewsActivity;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.query.Select;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsFragment extends Fragment {
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

    private LoadNewsAsyncTask mLoadNewsAsyncTask;

    private volatile int mPageNumber = LoadNewsAsyncTask.DEFAULT_PAGE;

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
        L.i(TAG, "onCreateView:" + mNewsType);
        mContentView = inflater.inflate(R.layout.fragment_news, container, false);
        initWidget();
        initData();
        initView();
        return mContentView;
    }

    @Override
    public void onResume() {
        L.i(TAG, "onResume:" + mNewsType);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.i(TAG, "onDestroyView:" + mNewsType);
        mPageNumber = LoadNewsAsyncTask.DEFAULT_PAGE;
        if(mLoadNewsAsyncTask!= null && mLoadNewsAsyncTask.getStatus() != AsyncTask.Status.FINISHED){
            mLoadNewsAsyncTask.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG, "onDestroy:" + mNewsType);
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
                if (position == mAdapter.getCount()) {
                    return;
                }
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
                        if(mLoadNewsAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
                            mLoadNewsAsyncTask = new LoadNewsAsyncTask(mContext, mNewsType);
                            mLoadNewsAsyncTask.execute();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mLoadNewsAsyncTask = new LoadNewsAsyncTask(mContext, mNewsType);
    }

    private void initView() {
        mLoadNewsAsyncTask.execute();
    }

    public void setNewsType(int type) {
        L.i(TAG, "news type:" + type);
        mNewsType = type;
    }


    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mLoadNewsAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                mLoadNewsAsyncTask.cancel(true);
            }
            mPageNumber = LoadNewsAsyncTask.DEFAULT_PAGE;
            mLoadNewsAsyncTask = new LoadNewsAsyncTask(mContext, mNewsType);
            mLoadNewsAsyncTask.execute();
        }
    };

    private class LoadNewsAsyncTask extends AsyncTask<Void, ResultNews, Integer> {
        private final String TAG = LoadNewsAsyncTask.class.getSimpleName();
        public static final int ERROR_SUCCESS = 0x00;
        public static final int ERROR_NETWORK_UNREACHABLE = 0x01;
        public static final int ERROR_NO_MORE = 0x02;
        public static final int ERROR_TIME_OUT = 0x03;

        public static final int DEFAULT_PAGE = 1;
        public static final int PAGE_SIZE = 20;

        private final String URL = MyApplication.Address + "/news/getNews/";
        private final long REQUEST_TIME_OUT = 20 * 1000;

        private int mNewsType;

        private Context mContext;
        private Gson mGson;
        private MyStringRequest mStringRequest;

        public LoadNewsAsyncTask(Context context, int newsType) {
            mContext = context;
            mNewsType = newsType;
//            mOperate = operate;
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
        protected void onPreExecute() {
            super.onPreExecute();
            if(mPageNumber == DEFAULT_PAGE && !fragment_news_refresh.isRefreshing()){
                fragment_news_refresh.setRefreshing(true);
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            L.i(TAG, "doInBackground:" + mNewsType + ", page number:" + mPageNumber);
            if (!Method.isNetworkAvailable(mContext)) {
                List<Data_DB_News> list = loadFromLocal();
                publishProgress(new ResultNews(list));
                if (list.size() > 0) {
                    mPageNumber++;
                }
                return ERROR_NETWORK_UNREACHABLE;
            } else {
                RequestFuture<String> requestFuture = RequestFuture.newFuture();
                mStringRequest = new MyStringRequest(Request.Method.GET, URL + "?type=" + mNewsType + "&page=" + mPageNumber, requestFuture, requestFuture);
                mStringRequest.setRetryPolicy(new MyRetryPolicy());
                MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
                try {
                    String response = requestFuture.get(REQUEST_TIME_OUT, TimeUnit.MILLISECONDS);
                    if (!TextUtils.isEmpty(response)) {
                        L.d("load news", response);
                        NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                        }.getType());
                        if (newsResponse.getCode() == NewsResponse.SUCESS_CODE) {
                            if (newsResponse.getList() != null && newsResponse.getList().size() > 0) {
                                int size = newsResponse.getList().size();
                                publishProgress(new ResultNews(newsResponse.getList()));
                                if (mPageNumber == 1) {
                                    SugarRecord.deleteAll(Data_DB_News.class);
                                }
                                SugarRecord.saveInTx(newsResponse.getList());
                                mPageNumber++;
                                if (size < PAGE_SIZE) {
                                    return ERROR_NO_MORE;
                                } else {
                                    return ERROR_SUCCESS;
                                }
                            } else {
                                return ERROR_NO_MORE;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    return ERROR_TIME_OUT;
                }
            }
            return ERROR_SUCCESS;
        }

        @Override
        protected final void onProgressUpdate(ResultNews... values) {
            super.onProgressUpdate(values);
            L.i(TAG, "onProgressUpdate");
            List<Data_DB_News> list = values[0].news;
            if (mPageNumber == 1) {
                mAdapter.clear();
                mAdapter.addAll(list);
            } else {
                mAdapter.addAll(list);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(fragment_news_refresh.isRefreshing()) {
                fragment_news_refresh.setRefreshing(false);
            }
            if (integer == ERROR_SUCCESS) {

            } else if (integer == ERROR_NETWORK_UNREACHABLE) {
                Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
            } else if (integer == ERROR_NO_MORE) {
                Tv_FooterText.setText(getString(R.string.no_more));
                Pb_FooterProgress.setVisibility(View.GONE);
            } else if(integer == ERROR_TIME_OUT){
                Toast.makeText(mContext, "请求超时", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(mStringRequest != null){
                mStringRequest.cancel();
            }
        }

        public List<Data_DB_News> loadFromLocal() {
            L.i(TAG, "loadFromLocal type :" + mNewsType + ",page:" + mPageNumber);
            int offset = (mPageNumber - 1) * PAGE_SIZE;
            return Select.from(Data_DB_News.class).where("type = ?", new String[]{mNewsType + ""})
                    .orderBy("createTime desc").limit(offset + "," + PAGE_SIZE).list();
        }
    }

    public static class ResultNews {
        final List<Data_DB_News> news;

        public ResultNews(List<Data_DB_News> list) {
            news = list;
        }
    }
}
