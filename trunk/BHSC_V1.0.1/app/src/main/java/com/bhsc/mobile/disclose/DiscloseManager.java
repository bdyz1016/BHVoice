package com.bhsc.mobile.disclose;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.disclose.model.DiscloseResponse;
import com.bhsc.mobile.net.MyRetryPolicy;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.net.ObjectResponse;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.bhsc.mobile.utils.WeakHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.NotNull;
import com.orm.query.Select;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 16/4/6.
 */
public class DiscloseManager {
    private final String TAG = DiscloseManager.class.getSimpleName();

    public interface OnDiscloseListener {
        void onLoaded(List<Data_DB_Disclose> list);

        void onRefreshed(List<Data_DB_Disclose> list);

        void deleteSuccess();

        void error(int error);
    }

    public static final int ERROR_LOAD_UNKNOWN = 0x1;
    public static final int ERROR_LOAD_NETWORK_UNREACHABLE = 0x2;
    public static final int ERROR_LOAD_NO_MORE = 0x3;

    public static final int ERROR_REFRESH_UNKNOWN = 0x10;
    public static final int ERROR_REFRESH_NETWORK_UNREACHABLE = 0x11;
    public static final int ERROR_REFRESH_NO_NEWS = 0x12;

    public static final int ERROR_DELETE_UNKNOWN = 0x20;
    public static final int ERROR_DELETE_NETWORK_UNREACHABLE = 0x21;

    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 1;

    private int mPageNumber = FIRST_PAGE;
    private int mLocalPage = FIRST_PAGE;
    private Context mContext;
    private OnDiscloseListener mListener;
    private WeakHandler mHandler;
    private MyStringRequest mRequest;
    private Gson mGson;

    public DiscloseManager(Context context, @NotNull OnDiscloseListener listener) {
        mContext = context;
        mListener = listener;
        mHandler = new WeakHandler();
        mGson = new Gson();
    }

    public void cancel() {
        if (mRequest != null && !mRequest.isCanceled()) {
            mRequest.cancel();
        }
    }

    public void loadDataFromLocal(long userId) {
        int offset = (mLocalPage - 1) * PAGE_SIZE;
        List<Data_DB_Disclose> list;
        if (userId < 0) {
            list = Select.from(Data_DB_Disclose.class).orderBy("create_time desc").limit(offset + "," + PAGE_SIZE).list();
        } else {
            list = Select.from(Data_DB_Disclose.class).where("user_id = " + userId).orderBy("create_time desc").limit(offset + "," + PAGE_SIZE).list();
        }
        if (list.size() > 0) {
            mLocalPage++;
            mListener.onLoaded(list);
        }
    }

    public void loadDataFromCloud(long userId) {
        if (!Method.isNetworkAvailable(mContext)) {
            int offset = (mLocalPage - 1) * PAGE_SIZE;
            List<Data_DB_Disclose> list;
            if (userId < 0) {
                list = Select.from(Data_DB_Disclose.class).orderBy("create_time desc").limit(offset + "," + PAGE_SIZE).list();
            } else {
                list = Select.from(Data_DB_Disclose.class).where("user_id = " + userId).orderBy("create_time desc").limit(offset + "," + PAGE_SIZE).list();
            }
            if (list.size() > 0) {
                mLocalPage++;
                mListener.onLoaded(list);
            }
            mListener.error(ERROR_LOAD_NETWORK_UNREACHABLE);
        } else {
            String url = MyApplication.Address + "/mood/getMoods?page=" + mPageNumber;
            if (userId > 0) {
                url = url + "&userId=" + userId;
            }
            cancel();
            mRequest = new MyStringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            L.i(TAG, response);
                            try {
                                response = URLDecoder.decode(response, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>() {
                            }.getType());
                            if (discloseResponse.getCode() == com.bhsc.mobile.net.Response.SUCESS_CODE) {
                                if (discloseResponse.getList().size() > 0) {
                                    mListener.onLoaded(discloseResponse.getList());
                                    if (mPageNumber == FIRST_PAGE) {
                                        SugarRecord.deleteAll(Data_DB_Disclose.class);
                                    }
                                    SugarRecord.saveInTx(discloseResponse.getList());
                                    mPageNumber++;
                                } else {
                                    mListener.error(ERROR_LOAD_NO_MORE);
                                }
                            } else {
                                mListener.error(ERROR_LOAD_UNKNOWN);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mListener.error(ERROR_LOAD_UNKNOWN);
                }
            });
            mRequest.setRetryPolicy(new MyRetryPolicy());
            MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
        }
    }


    public void refresh(long userId) {
        mPageNumber = FIRST_PAGE;
        if (Method.isNetworkAvailable(mContext)) {
            String url = MyApplication.Address + "/mood/getMoods?page=" + mPageNumber;
            if (userId > 0) {
                url = url + "&userId=" + userId;
            }
            cancel();
            mRequest = new MyStringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            L.i(TAG, response);
                            DiscloseResponse discloseResponse = mGson.fromJson(response, new TypeToken<DiscloseResponse>() {
                            }.getType());
                            if (discloseResponse.getCode() == com.bhsc.mobile.net.Response.SUCESS_CODE) {
                                if (discloseResponse.getList().size() > 0) {
                                    SugarRecord.deleteAll(Data_DB_Disclose.class);
                                    SugarRecord.saveInTx(discloseResponse.getList());
                                    mListener.onRefreshed(discloseResponse.getList());
                                    mPageNumber++;
                                } else {
                                    mListener.error(ERROR_REFRESH_NO_NEWS);
                                }
                            } else {
                                mListener.error(ERROR_REFRESH_UNKNOWN);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mListener.error(ERROR_REFRESH_NETWORK_UNREACHABLE);
                }
            });
            mRequest.setRetryPolicy(new MyRetryPolicy());
            MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
        } else {
            mListener.error(ERROR_REFRESH_NETWORK_UNREACHABLE);
        }
    }

    public void deleteDisclose(final long id) {
        if (!Method.isNetworkAvailable(mContext)) {
            mListener.error(ERROR_DELETE_NETWORK_UNREACHABLE);
            return;
        }
        cancel();
        String url = MyApplication.Address + "/mood/deleteMoodByid";
        mRequest = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    mListener.error(ERROR_DELETE_UNKNOWN);
                    return;
                }
                ObjectResponse objectResponse = mGson.fromJson(response, new TypeToken<ObjectResponse>() {
                }.getType());
                if (objectResponse.getCode() == ObjectResponse.SUCESS_CODE) {
                    mListener.deleteSuccess();
                } else {
                    mListener.error(ERROR_DELETE_UNKNOWN);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.error(ERROR_DELETE_UNKNOWN);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id + "");
                return params;
            }
        };
        mRequest.setRetryPolicy(new MyRetryPolicy());
        MySingleton.getInstance(mContext).getRequestQueue().add(mRequest);
    }
}
