package com.bhsc.mobile.news;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.news.model.Data_DB_News;
import com.bhsc.mobile.news.model.NewsResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.NotNull;
import com.orm.query.Select;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhanglei on 16/3/30.
 */
public class NewsManager {
    private final String TAG = NewsManager.class.getSimpleName();

    public static final int PAGE_SIZE = 20;
    public static final int START_PAGE = 1;
    private final String URL = MyApplication.Address + "/news/getNews/";

    public static final int ERROR_LOAD_UNKNOWN = 1;
    public static final int ERROR_LOAD_NETWORK_UNREACHABLE = 2;
    public static final int ERROR_LOAD_NO_MORE = 3;

    public static final int ERROR_REFRESH_UNKNOWN = 4;
    public static final int ERROR_REFRESH_NETWORK_UNREACHABLE = 5;
    public static final int ERROR_REFRESH_NO_NEWS = 6;


    public interface OnNewsListener {
        void loaded(List<Data_DB_News> list);

        void refreshed(List<Data_DB_News> list);

        void error(int error);
    }

    public interface OnSearchListener {
        void result(List<Data_DB_News> list);

        void error();
    }

    private OnNewsListener mListener = new OnNewsListener() {
        @Override
        public void loaded(List<Data_DB_News> list) {

        }

        @Override
        public void refreshed(List<Data_DB_News> list) {

        }

        @Override
        public void error(int error) {

        }
    };

    /**
     * 服务器请求加载分页
     */
    private int mCurrentPage = START_PAGE;
    /**
     * 本地加载分页
     */
    private int mLocalPage = START_PAGE;
    private Context mContext;
    private MyStringRequest mStringRequest;
    private Gson mGson;

    public NewsManager(Context context) {
        mContext = context;
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

    public void setNewsListener(@NotNull OnNewsListener listener) {
        this.mListener = listener;
    }

    /**
     * 加载本地缓存
     *
     * @param type 新闻类型
     */
    public synchronized void loadFromLocal(int type) {
        int offset = (mLocalPage - 1) * PAGE_SIZE;
        List<Data_DB_News> list = Select.from(Data_DB_News.class).where("type = ?", new String[]{type + ""})
                .orderBy("createTime desc").limit(offset + "," + PAGE_SIZE).list();
//        List<Data_DB_News> list = Select.from(Data_DB_News.class)
//                .where("type = ?", new String[]{type + ""})
//                .orderBy("createTime desc")
//                .list();
        if (list.size() > 0) {
            mLocalPage++;
            mListener.loaded(list);
        }
    }

    /**
     * 网络可用从服务器加载，不可用从本地加载
     *
     * @param type 新闻类型
     */
    public synchronized void loadFromCloud(int type) {
        L.i(TAG, "load");
        if (!Method.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
            int offset = (mLocalPage - 1) * PAGE_SIZE;
            List<Data_DB_News> list = Select.from(Data_DB_News.class).where("type = ?", new String[]{type + ""})
                    .orderBy("createTime desc").limit(offset + "," + PAGE_SIZE).list();
            if (list.size() > 0) {
                mLocalPage++;
                mListener.loaded(list);
            }
            return;
        }
        cancel();
        mStringRequest = new MyStringRequest(Request.Method.GET, URL + "?type=" + type + "&page=" + mCurrentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    L.d("load news", response);
                    NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                    }.getType());
                    if (newsResponse.getCode() == NewsResponse.SUCESS_CODE) {
                        if (newsResponse.getList() != null && newsResponse.getList().size() > 0) {
                            mListener.loaded(newsResponse.getList());
                            if (mCurrentPage == START_PAGE) {
                                SugarRecord.deleteAll(Data_DB_News.class);
                            }
                            mCurrentPage++;
                            SugarRecord.saveInTx(newsResponse.getList());
                        } else {
                            mListener.error(ERROR_LOAD_NO_MORE);
                        }
                    } else {
                        mListener.error(ERROR_LOAD_UNKNOWN);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.error(ERROR_LOAD_UNKNOWN);
            }
        });
        mStringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
    }

    /**
     * 刷新新闻
     *
     * @param type 新闻类型
     */
    public synchronized void refreshed(int type) {
        cancel();
        mCurrentPage = START_PAGE;
        if (!Method.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        mStringRequest = new MyStringRequest(Request.Method.GET, URL + "?type=" + type + "&page=" + mCurrentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    L.d(TAG, response);
                    NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                    }.getType());
                    if (newsResponse.getCode() == NewsResponse.SUCESS_CODE) {
                        if (newsResponse.getList() != null && newsResponse.getList().size() > 0) {
                            mCurrentPage = START_PAGE + 1;
                            SugarRecord.deleteAll(Data_DB_News.class);
                            SugarRecord.saveInTx(newsResponse.getList());
                            mListener.refreshed(newsResponse.getList());
                        } else {
                            mListener.error(ERROR_REFRESH_NO_NEWS);
                        }
                    } else {
                        mListener.error(ERROR_REFRESH_UNKNOWN);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.error(ERROR_REFRESH_UNKNOWN);
            }
        });
        mStringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
    }

    public synchronized void search(String keyword, final OnSearchListener listener) {
        MyStringRequest request = new MyStringRequest(Request.Method.GET, MyApplication.Address + "/news/search?keyword=" + keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    L.d(TAG, response);
                    NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                    }.getType());
                    if (newsResponse.getCode() == NewsResponse.SUCESS_CODE) {
                        listener.result(newsResponse.getList());
                    } else {
                        listener.error();
                    }
                } else {
                    listener.error();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
            }
        });
        request.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(request);
    }

    public synchronized void cancel() {
        if (mStringRequest != null && !mStringRequest.isCanceled()) {
            mStringRequest.cancel();
        }
    }
}
