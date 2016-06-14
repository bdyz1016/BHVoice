package com.bhsc.mobile.news.newsdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.baseclass.Manager;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.news.model.Data_DB_Detail;
import com.bhsc.mobile.news.model.DetailResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;

import java.lang.reflect.Field;

/**
 * Created by zhanglei on 16/5/31.
 */
public class NewsStore extends Manager{
    private final String TAG = NewsStore.class.getSimpleName();
    public interface OnNewsListener{
        void onLoaded(Data_DB_Detail data);
        void error(int error);
    }

    private OnNewsListener mListener = new OnNewsListener() {
        @Override
        public void onLoaded(Data_DB_Detail data) {

        }

        @Override
        public void error(int error) {

        }
    };

    private final String URL;
    private Context mContext;
    private Gson mGson;

    public NewsStore(@NonNull Context context, @NonNull OnNewsListener listener){
        mContext = context;
        mListener = listener;
        URL = MyApplication.Address + "/news/getNewsById";
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

    public void getNews(long id){
        Data_DB_Detail detail = SugarRecord.findById(Data_DB_Detail.class, id);
        if(detail != null){
            mListener.onLoaded(detail);
        } else if(Method.isNetworkAvailable(mContext)){
            String url = URL + "?id=" + id;
            MyStringRequest request = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    L.i(TAG, "response:" + response);
                    if(TextUtils.isEmpty(response)){
                        mListener.error(ERROR_SERVER_ISSUES);
                        return;
                    }
                    DetailResponse detailResponse = mGson.fromJson(response, new TypeToken<DetailResponse>(){}.getType());
                    if(detailResponse != null){
                        if(detailResponse.getCode() == DetailResponse.SUCESS_CODE){
                            mListener.onLoaded(detailResponse.getNews());
                            detailResponse.getNews().save();
                        } else {
                            mListener.error(ERROR_UNKNOWN);
                        }
                    } else {
                        mListener.error(ERROR_SERVER_ISSUES);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mListener.error(ERROR_NETWORK_UNREACHABLE);
                }
            });
            request.setRetryPolicy(new MyRetryPolicy());
            MySingleton.getInstance(mContext).addToRequestQueue(request);
        } else {
            mListener.error(ERROR_NETWORK_UNREACHABLE);
        }
    }
}
