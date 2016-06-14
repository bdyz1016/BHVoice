package com.bhsc.mobile.userpages;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.net.MySingleton;
import com.bhsc.mobile.net.MyStringRequest;
import com.bhsc.mobile.net.RequestError;
import com.bhsc.mobile.net.UploadFile;
import com.bhsc.mobile.userpages.model.Data_DB_User;
import com.bhsc.mobile.userpages.model.UserResponse;
import com.bhsc.mobile.utils.ImageUtil;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhanglei on 16/3/31.
 */
public class UserManager {
    private final String TAG = UserManager.class.getSimpleName();

    public static final int ERROR_USERNAME_EMPTY = 0X1;
    public static final int ERROR_USERNAME_FORMAT = 0X2;
    public static final int ERROR_PASSWORD_EMPTY = 0X3;
    public static final int ERROR_PASSWORD_FORMAT = 0X4;
    public static final int ERROR_USERNAME_WRONG = 0X5;
    public static final int ERROR_PASSWORD_WRONG = 0X6;
    public static final int ERROR_PASSWORD_INCONSISTENT = 0X7;
    public static final int ERROR_USERNAME_EXIST = 0X8;
    public static final int ERROR_NICKNAME_EXIST = 0X9;
    public static final int ERROR_STATUS_EMPTY = 0XA;
    public static final int ERROR_FILE_NOT_EXIST = 0XB;
    public static final int ERROR_NETWORK_UNREACHABLE = 0XC;
    public static final int ERROR_REQUEST_TIMEOUT = 0XD;
    public static final int ERROR_UNKNOWN = 0X20;

    public interface OnLoginListener{
        void success();
        void failed(RequestError error);
    }

    public interface OnEditUserInfoListener{
        void success();
        void failed(RequestError error);
    }

    public static final int PHOTO_SIZE = 200;

    private Context mContext;
    private MyStringRequest mLoginRequest;
    private UploadPhotoTask mUploadPhotoTask;
    private SetUserNameTask mSetUserNameTask;
    private SetStatusTask mSetStatusTask;
    private ResetPasswordTask mResetPasswordTask;

    public UserManager(Context context){
        mContext = context;
    }

    public void setUserPhoto(String filePath, OnEditUserInfoListener listener){
        if(mUploadPhotoTask != null && !mUploadPhotoTask.isCancelled()){
            mUploadPhotoTask.cancel(true);
        }
        mUploadPhotoTask = new UploadPhotoTask(mContext, listener);
        mUploadPhotoTask.execute(filePath);
    }

    public void setUserName(String name, OnEditUserInfoListener listener){
        if(mSetUserNameTask != null && !mSetUserNameTask.isCancelled()){
            mSetUserNameTask.cancel(true);
        }
        mSetUserNameTask = new SetUserNameTask(mContext, listener);
        mSetUserNameTask.execute(name);
    }

    public void setStatus(String status, OnEditUserInfoListener listener){
        if(mSetStatusTask != null && !mSetStatusTask.isCancelled()){
            mSetStatusTask.cancel(true);
        }
        mSetStatusTask = new SetStatusTask(mContext, listener);
        mSetStatusTask.execute(status);
    }

    public void resetPassword(String newPassword, String repeatPassword, OnEditUserInfoListener listener){
        if(mResetPasswordTask != null && !mResetPasswordTask.isCancelled()){
            mResetPasswordTask.cancel(true);
        }
        mResetPasswordTask = new ResetPasswordTask(mContext, listener);
        mResetPasswordTask.execute(newPassword,repeatPassword);
    }

    public void loginThirdParty(final String openId, final String type, OnLoginListener listener){
        if(mLoginRequest != null && !mLoginRequest.isCanceled()){
            mLoginRequest.cancel();
        }
        mLoginRequest = new MyStringRequest(Request.Method.POST, MyApplication.Address + "/user/thirdLogin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                L.i("loginThirdParty", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("openId", openId);
                params.put("type", type);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(mLoginRequest);
    }

    public void cancel(){
        if(mLoginRequest != null && !mLoginRequest.isCanceled()){
            mLoginRequest.cancel();
        }
        if(mUploadPhotoTask != null && !mUploadPhotoTask.isCancelled()){
            mUploadPhotoTask.cancel(true);
        }
        if(mSetUserNameTask != null && !mSetUserNameTask.isCancelled()){
            mSetUserNameTask.cancel(true);
        }
        if(mSetStatusTask != null && !mSetStatusTask.isCancelled()){
            mSetStatusTask.cancel(true);
        }
    }

    private static class UploadPhotoTask extends AsyncTask<String, Integer, Integer>{
        private final String TAG = UploadPhotoTask.class.getSimpleName();

        private Gson mGson;
        private Context mContext;
        private WeakReference<OnEditUserInfoListener> weakReference;
        public UploadPhotoTask(Context context, OnEditUserInfoListener listener){
            mContext = context;
            weakReference = new WeakReference<>(listener);
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
        protected Integer doInBackground(String... params) {
            String filePath = params[0];
            File image = new File(filePath);
            if(!image.exists()){
                return ERROR_FILE_NOT_EXIST;
            }
            Bitmap bitmap = ImageUtil.decodeSampledBitmapFromSource(filePath, PHOTO_SIZE, PHOTO_SIZE);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(image, false);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!Method.isNetworkAvailable(mContext)){
                return ERROR_NETWORK_UNREACHABLE;
            }
            String url = MyApplication.Address + "/user/updateHeadUrl?userId=" + getUser().getId();
            L.i(TAG, "add mood url:" + url);
            List<File> fileList = new ArrayList<>();
            fileList.add(image);
            try {
                String response = UploadFile.uploadMultiFileSync(url, null, fileList, null);
                L.i(TAG, "set photo:" + response);
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    UserManager.login(userResponse.getUser());
                    L.i(TAG, "login success!");
                    image.delete();
                    return 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ERROR_UNKNOWN;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(weakReference.get() != null){
                if(integer == 0){
                    weakReference.get().success();
                } else {
                    RequestError error = new RequestError();
                    error.setErrorCode(integer);
                    if(integer == ERROR_NETWORK_UNREACHABLE){
                        error.setMessage("网络异常");
                    } else if(integer == ERROR_FILE_NOT_EXIST){
                        error.setMessage("文件不存在");
                    } else if(integer == ERROR_UNKNOWN){
                        error.setMessage("未知错误");
                    }
                    weakReference.get().failed(error);
                }
            }
        }
    }

    private static class SetUserNameTask extends AsyncTask<String, Integer, Integer>{
        private final String TAG = SetUserNameTask.class.getSimpleName();
        private Gson mGson;
        private Context mContext;
        private WeakReference<OnEditUserInfoListener> weakReference;
        public SetUserNameTask(Context context, OnEditUserInfoListener listener){
            mContext = context;
            weakReference = new WeakReference<>(listener);
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
        protected Integer doInBackground(String... params) {
            final String userName = params[0];
            if(TextUtils.isEmpty(userName)){
                return ERROR_USERNAME_EMPTY;
            }
            if(!Method.isNetworkAvailable(mContext)){
                return ERROR_NETWORK_UNREACHABLE;
            }
            RequestFuture<String> future = RequestFuture.newFuture();
            MyStringRequest request = new MyStringRequest(Request.Method.POST, MyApplication.Address + "/user/updateUsername", future, future){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("newUserName", userName);
                    params.put("userId", getUser().getId() + "");
                    return params;
                }
            };
            MySingleton.getInstance(mContext).addToRequestQueue(request);
            try {
                String response = future.get(30, TimeUnit.SECONDS);
                if(TextUtils.isEmpty(response)){
                    return ERROR_UNKNOWN;
                }
                L.i(TAG, "set username:" + response);
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    UserManager.updateUsername(userName);
                    L.i(TAG, "login success!");
                    return 0;
                } else if(userResponse.getCode() == 202){
                    return ERROR_NICKNAME_EXIST;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                return ERROR_REQUEST_TIMEOUT;
            }
            return ERROR_UNKNOWN;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(weakReference.get() != null){
                if(integer == 0){
                    weakReference.get().success();
                } else {
                    RequestError error = new RequestError();
                    error.setErrorCode(integer);
                    if(integer == ERROR_NETWORK_UNREACHABLE){
                        error.setMessage("网络异常");
                    } else if(integer == ERROR_REQUEST_TIMEOUT){
                        error.setMessage("请求超时");
                    } else if(integer == ERROR_UNKNOWN){
                        error.setMessage("未知错误");
                    } else if(integer == ERROR_NICKNAME_EXIST){
                        error.setMessage("昵称已存在");
                    }
                    weakReference.get().failed(error);
                }
            }
        }
    }

    private static class SetStatusTask extends AsyncTask<String, Integer, Integer>{
        private final String TAG = SetStatusTask.class.getSimpleName();
        private Gson mGson;
        private Context mContext;
        private WeakReference<OnEditUserInfoListener> weakReference;
        public SetStatusTask(Context context, OnEditUserInfoListener listener){
            mContext = context;
            weakReference = new WeakReference<>(listener);
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
        protected Integer doInBackground(String... params) {
            final String status = params[0];
            if(TextUtils.isEmpty(status)){
                return ERROR_STATUS_EMPTY;
            }
            if(!Method.isNetworkAvailable(mContext)){
                return ERROR_NETWORK_UNREACHABLE;
            }
            RequestFuture<String> future = RequestFuture.newFuture();
            MyStringRequest request = new MyStringRequest(Request.Method.POST, MyApplication.Address + "/user/updateSign", future, future){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("sign", status);
                    params.put("userId", getUser().getId() + "");
                    return params;
                }
            };
            MySingleton.getInstance(mContext).addToRequestQueue(request);
            try {
                String response = future.get(30, TimeUnit.SECONDS);
                if(TextUtils.isEmpty(response)){
                    return ERROR_UNKNOWN;
                }
                L.i(TAG, "set status:" + response);
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    UserManager.updateStatus(status);
                    L.i(TAG, "login success!");
                    return 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                return ERROR_REQUEST_TIMEOUT;
            }
            return ERROR_UNKNOWN;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(weakReference.get() != null){
                if(integer == 0){
                    weakReference.get().success();
                } else {
                    RequestError error = new RequestError();
                    error.setErrorCode(integer);
                    if(integer == ERROR_NETWORK_UNREACHABLE){
                        error.setMessage("网络异常");
                    } else if(integer == ERROR_REQUEST_TIMEOUT){
                        error.setMessage("请求超时");
                    } else if(integer == ERROR_UNKNOWN){
                        error.setMessage("未知错误");
                    }
                    weakReference.get().failed(error);
                }
            }
        }
    }

    private static class ResetPasswordTask extends AsyncTask<String, Integer, Integer>{
        private final String TAG = ResetPasswordTask.class.getSimpleName();
        private Gson mGson;
        private Context mContext;
        private WeakReference<OnEditUserInfoListener> weakReference;
        public ResetPasswordTask(Context context, OnEditUserInfoListener listener){
            mContext = context;
            weakReference = new WeakReference<>(listener);
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
        protected Integer doInBackground(String... params) {
            final String newPassword = params[0];
            final String repeatPassword = params[1];
            int error = checkPassword(newPassword);
            if(error != 0){
                return error;
            } else if(!newPassword.equals(repeatPassword)){
                return ERROR_PASSWORD_INCONSISTENT;
            }
            if(!Method.isNetworkAvailable(mContext)){
                return ERROR_NETWORK_UNREACHABLE;
            }
            RequestFuture<String> future = RequestFuture.newFuture();
            MyStringRequest request = new MyStringRequest(Request.Method.POST, MyApplication.Address + "/user/updatePwd", future, future){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("password", newPassword);
                    params.put("userId", getUser().getId() + "");
                    params.put("oldPwd", getUser().getPassword());
                    return params;
                }
            };
            MySingleton.getInstance(mContext).addToRequestQueue(request);
            try {
                String response = future.get(30, TimeUnit.SECONDS);
                if(TextUtils.isEmpty(response)){
                    return ERROR_UNKNOWN;
                }
                L.i(TAG, "change password:" + response);
                UserResponse userResponse = mGson.fromJson(response, UserResponse.class);
                if (userResponse.getCode() == UserResponse.SUCESS_CODE) {
                    UserManager.updatePassword(newPassword);
                    L.i(TAG, "login success!");
                    return 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                return ERROR_REQUEST_TIMEOUT;
            }
            return ERROR_UNKNOWN;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(weakReference.get() != null){
                if(integer == 0){
                    weakReference.get().success();
                } else {
                    RequestError error = new RequestError();
                    error.setErrorCode(integer);
                    if(integer == ERROR_NETWORK_UNREACHABLE){
                        error.setMessage("网络异常");
                    } else if(integer == ERROR_REQUEST_TIMEOUT){
                        error.setMessage("请求超时");
                    } else if(integer == ERROR_UNKNOWN){
                        error.setMessage("未知错误");
                    } else if(integer == ERROR_PASSWORD_EMPTY){
                        error.setMessage("密码不能为空");
                    } else if(integer == ERROR_PASSWORD_FORMAT){
                        error.setMessage("密码格式错误");
                    } else if(integer == ERROR_PASSWORD_INCONSISTENT){
                        error.setMessage("两次输入密码不一致");
                    }
                    weakReference.get().failed(error);
                }
            }
        }
    }

    public static synchronized boolean isLogin(){
        return SugarRecord.count(Data_DB_User.class) > 0;
    }

    public static synchronized void login(Data_DB_User user){
        if(user == null){
            return;
        }
        user.save();
    }

    public static synchronized void updateHeadUrl(String headUrl){
        Data_DB_User user = SugarRecord.first(Data_DB_User.class);
        user.setHeadurl(headUrl);
        user.save();
    }

    public static synchronized void updateUsername(String username){
        Data_DB_User user = SugarRecord.first(Data_DB_User.class);
        user.setUsername(username);
        user.save();
    }

    public static synchronized void updateStatus(String status){
        Data_DB_User user = SugarRecord.first(Data_DB_User.class);
        user.setStatus(status);
        user.save();
    }

    public static synchronized void updatePassword(String password){
        Data_DB_User user = SugarRecord.first(Data_DB_User.class);
        user.setPassword(password);
        user.save();
    }

    public static synchronized boolean logout(){
        return SugarRecord.deleteAll(Data_DB_User.class) > 0;
    }

    public static synchronized Data_DB_User getUser(){
        return SugarRecord.first(Data_DB_User.class);
    }

    public static int checkPassword(String password){
        if(TextUtils.isEmpty(password)){
            return ERROR_PASSWORD_EMPTY;
        } else if(password.length() > 128 || password.length() < 6){
            return ERROR_PASSWORD_FORMAT;
        }
        return 0;
    }
}
