package com.bhsc.mobile.comment;

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
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Created by zhanglei on 16/4/27.
 */
public class CommentManager {

    public static final int TYPE_NEWS = 1;
    public static final int TYPE_NEWS_COMMENT = 2;
    public static final int TYPE_DISCLOSE = 3;
    public static final int TYPE_DISCLOSE_COMMENT = 4;
//    type(type=1对新闻的评论type=0对评论的评论，type=3对爆料的评论，type=4对爆料的评论进行评论)

    private Context mContext;
    private Gson mGson;
    public CommentManager(Context context){
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

    public void addComment(long newsId, String content, int type){
        if(TextUtils.isEmpty(content)){
            Toast.makeText(mContext, "内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Method.isNetworkAvailable(mContext)){
            Toast.makeText(mContext, "网络不可用!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            content = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = MyApplication.Address + "/comment/addComment?type=" + type + "&fatherId="
                + newsId + "&userId=" + newsId + "&content=" + content;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                    Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ObjectResponse resp = mGson.fromJson(response, ObjectResponse.class);
                if(resp.getCode() == ObjectResponse.SUCESS_CODE){
                    Toast.makeText(mContext, "发布成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(stringRequest);
    }
}
