package com.bhsc.mobile.comment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.comment.model.AddCommentResponse;
import com.bhsc.mobile.comment.model.CommentResponse;
import com.bhsc.mobile.comment.model.Data_DB_Comment;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.net.ObjectResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 16/4/27.
 */
public class CommentManager {
    private final String TAG = CommentManager.class.getSimpleName();
    public static final int TYPE_NEWS = 1;
    public static final int TYPE_NEWS_COMMENT = 2;
    public static final int TYPE_DISCLOSE = 3;
    public static final int TYPE_DISCLOSE_COMMENT = 4;
//    type(type=1对新闻的评论type=0对评论的评论，type=3对爆料的评论，type=4对爆料的评论进行评论)

    private Context mContext;
    private Gson mGson;
    private MyStringRequest mRequest;
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

    public void cancel(){
        if(mRequest != null && !mRequest.isCanceled()){
            mRequest.cancel();
        }
    }

    public void addComment(final long userId, final long newsId, String content, final int type, final OnAddCommentListener listener){
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
        String url = MyApplication.Address + "/comment/addComment";
        final String finalContent = content;
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                    Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
                    return;
                }
                AddCommentResponse resp = mGson.fromJson(response, AddCommentResponse.class);
                if(resp.getCode() == ObjectResponse.SUCESS_CODE){
                    Toast.makeText(mContext, "发布成功!", Toast.LENGTH_SHORT).show();
                    if(listener != null) {
                        listener.success(resp.getComment());
                    }
                } else {
                    Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
                    if(listener != null) {
                        listener.failed();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "发布评论失败!", Toast.LENGTH_SHORT).show();
                if(listener != null) {
                    listener.failed();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", type + "");
                params.put("fatherId", newsId + "");
                params.put("userId", userId + "");
                params.put("content", finalContent);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(stringRequest);
    }

    public void loadComment(long fatherId, int type, final OnCommentListener listener){
        if(!Method.isNetworkAvailable(mContext)){
            Toast.makeText(mContext, "网络不可用!", Toast.LENGTH_SHORT).show();
            return;
        }
        cancel();
        String url = MyApplication.Address + "/comment/findComments?type=" + type + "&fatherId=" + fatherId;
        L.i(TAG, "url:" + url);
        mRequest = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                    listener.onLoadFailed();
                    return;
                }
                L.i(TAG, response);
                try {
                    response = URLDecoder.decode(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                CommentResponse commentResponse = mGson.fromJson(response, new TypeToken<CommentResponse>(){}.getType());
                if(commentResponse.getCode() == ObjectResponse.SUCESS_CODE){
                    listener.onLoaded(commentResponse.getList());
                }else {
                    listener.onLoadFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onLoadFailed();
            }
        });
        mRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
    }

    public interface OnCommentListener{
        void onLoaded(List<Data_DB_Comment> list);
        void onLoadFailed();
    }

    public interface OnAddCommentListener{
        void success(Data_DB_Comment comment);
        void failed();
    }
}
