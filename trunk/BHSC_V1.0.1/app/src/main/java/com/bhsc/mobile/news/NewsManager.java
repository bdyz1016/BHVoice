package com.bhsc.mobile.news;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.ObjectResponse;
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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zhanglei on 16/3/30.
 */
public class NewsManager {
    private final String TAG = NewsManager.class.getSimpleName();

    public static final int PAGE_SIZE = 20;
    public static final int START_PAGE = 1;
    private final String URL = MyApplication.Address + "/news/getNews/";

    public interface OnNewsListener {
        void loaded(List<Data_DB_News> list);

        void refreshed(List<Data_DB_News> list);
    }

    private OnNewsListener mListener = new OnNewsListener() {
        @Override
        public void loaded(List<Data_DB_News> list) {

        }

        @Override
        public void refreshed(List<Data_DB_News> list) {

        }
    };

    private int mCurrentPage = START_PAGE;
    private Context mContext;
    private StringRequest mStringRequest;
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

    public synchronized void load(int type) {
        L.i(TAG, "load");
        if (!Method.isNetworkAvailable(mContext)) {
            int offset = mCurrentPage * PAGE_SIZE;
            if (SugarRecord.count(Data_DB_News.class, "type = ?", new String[]{type + ""}, null, null, PAGE_SIZE + "," + offset) > 0) {
                mCurrentPage++;
                List<Data_DB_News> list = Select.from(Data_DB_News.class).where("type = ?", new String[]{type + ""}).orderBy("createTime desc").limit(PAGE_SIZE + "," + offset).list();
                mListener.loaded(list);
            }
            return;
        }
        cancel();
        mStringRequest = new StringRequest(Request.Method.GET, URL + "?type=" + type + "&page=" + mCurrentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    L.d("zhanglei", response);
                    try {
                        response = URLDecoder.decode(response, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                    }.getType());
                    if (newsResponse.getCode() == NewsResponse.SUCESS_CODE) {
                        mListener.loaded(newsResponse.getList());
                        if(newsResponse.getList() != null && newsResponse.getList().size() > 0) {
                            if (mCurrentPage == START_PAGE) {
                                SugarRecord.deleteAll(Data_DB_News.class);
                            }
                            mCurrentPage++;
                            SugarRecord.saveInTx(newsResponse.getList());
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mStringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
    }

    public synchronized void refreshed(int type) {
        cancel();
        mCurrentPage = START_PAGE;
        if (!Method.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        mStringRequest = new StringRequest(Request.Method.GET, URL + "?type=" + type + "&page=" + mCurrentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    L.d("zhanglei", response);
                    NewsResponse newsResponse = mGson.fromJson(response, new TypeToken<NewsResponse>() {
                    }.getType());
                    if (newsResponse.getCode() == NewsResponse.SUCESS_CODE &&
                            newsResponse.getList() != null && newsResponse.getList().size() > 0) {
                        mCurrentPage = START_PAGE + 1;
                        SugarRecord.deleteAll(Data_DB_News.class);
                        SugarRecord.saveInTx(newsResponse.getList());
                        mListener.refreshed(newsResponse.getList());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mStringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mStringRequest);
    }

    public synchronized void cancel() {
        if (mStringRequest != null && !mStringRequest.isCanceled()) {
            mStringRequest.cancel();
        }
    }
}
