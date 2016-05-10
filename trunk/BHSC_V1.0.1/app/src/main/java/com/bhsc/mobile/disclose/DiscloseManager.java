package com.bhsc.mobile.disclose;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.disclose.model.DiscloseResponse;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.bhsc.mobile.utils.WeakHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by zhanglei on 16/4/6.
 */
public class DiscloseManager {
    private final String TAG = DiscloseManager.class.getSimpleName();
    public interface OnDiscloseListener{
        void onLoaded(List<Data_DB_Disclose> list);
        void onRefreshed(List<Data_DB_Disclose> list);
        void error(int error);
    }

    public static final int ERROR_NETWORK_UNREACHABLE = 0;

    public static final int FIRST_PAGE = 1;

    private int mPageNumber = FIRST_PAGE;
    private Context mContext;
    private OnDiscloseListener mListener;
    private WeakHandler mHandler;
    private MyStringRequest mRequest;
    private Gson mGson;

    public DiscloseManager(Context context, @NotNull OnDiscloseListener listener){
        mContext = context;
        mListener = listener;
        mHandler = new WeakHandler();
        mGson = new Gson();
    }

    public void loadData(){
        if(Method.isNetworkAvailable(mContext)){
            mRequest = new MyStringRequest(Request.Method.GET, MyApplication.Address + "/mood/getMoods?page=" + mPageNumber,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            L.i(TAG, response);
                            try {
                                response = URLDecoder.decode(response, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>(){}.getType());
                            if(discloseResponse.getCode() == com.bhsc.mobile.net.Response.SUCESS_CODE){
                                mListener.onLoaded(discloseResponse.getList());
                                if(discloseResponse.getList().size() > 0){
                                    if(mPageNumber == FIRST_PAGE){
                                        SugarRecord.deleteAll(Data_DB_Disclose.class);
                                    }
                                    SugarRecord.saveInTx(discloseResponse.getList());
                                    mPageNumber++;
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mRequest.setRetryPolicy(new MyRetryPolicy());
            MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
        } else {
            List<Data_DB_Disclose> list = SugarRecord.listAll(Data_DB_Disclose.class, "create_time desc");
            mListener.onLoaded(list);
        }
    }

    public void loadData(long userId){
        List<Data_DB_Disclose> list = SugarRecord.find(Data_DB_Disclose.class, "user_id = ?", new String[]{userId + ""}, null, "create_time desc", null);
        mListener.onLoaded(list);
//        if(Method.isNetworkAvailable(mContext)){
//            mRequest = new MyStringRequest(Request.Method.GET, MyApplication.Address + "/mood/getMoods?page=" + mPageNumber + "&userId=" + userId,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            L.i(TAG, response);
//                            try {
//                                response = URLDecoder.decode(response, "UTF-8");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>(){}.getType());
//                            if(discloseResponse.getCode() == com.bhsc.mobile.net.Response.SUCESS_CODE){
//                                if(discloseResponse.getList().size() > 0){
//                                    mListener.onLoaded(discloseResponse.getList());
//                                    if(mPageNumber == FIRST_PAGE){
//                                        SugarRecord.deleteAll(Data_DB_Disclose.class);
//                                    }
//                                    SugarRecord.saveInTx(discloseResponse.getList());
//                                    mPageNumber++;
//                                }
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            });
//            mRequest.setRetryPolicy(new MyRetryPolicy());
//            MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
//        } else {
//            List<Data_DB_Disclose> list = SugarRecord.find(Data_DB_Disclose.class, "user_id = ?", new String[]{userId + ""}, null, "create_time desc", null);
//            mListener.onLoaded(list);
//        }
    }

    public void refresh(){
        mPageNumber = FIRST_PAGE;
        if(Method.isNetworkAvailable(mContext)){
            mRequest = new MyStringRequest(Request.Method.GET, MyApplication.Address + "/mood/getMoods?page=" + mPageNumber,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            L.i(TAG, response);
                            DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>(){}.getType());
                            if(discloseResponse.getCode() == com.bhsc.mobile.net.Response.SUCESS_CODE){
                                if(discloseResponse.getList().size() > 0){
                                    SugarRecord.deleteAll(Data_DB_Disclose.class);
                                    SugarRecord.saveInTx(discloseResponse.getList());
                                    mListener.onRefreshed(discloseResponse.getList());
                                    mPageNumber++;
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mRequest.setRetryPolicy(new MyRetryPolicy());
            MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
        } else {
            mListener.error(ERROR_NETWORK_UNREACHABLE);
        }
    }
}
