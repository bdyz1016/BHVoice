package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.datalcass.Data_DB_User;
import com.bhsc.mobile.main.BHApplication;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.net.Response;
import com.bhsc.mobile.net.httpPost;
import com.bhsc.mobile.userpages.event.UserEvent;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 15-10-7.
 */
public class UserPresenter {
    private final String TAG = UserPresenter.class.getSimpleName();

    private static UserPresenter sUserPresenter;

    public static UserPresenter getInstance(Context context){
        if(sUserPresenter == null){
            synchronized (UserPresenter.class) {
                if(sUserPresenter == null) {
                    sUserPresenter = new UserPresenter(context);
                }
            }
        }
        return sUserPresenter;
    }

    private ExecutorService mExecutorService;
    private DataBaseTools mDataBaseTools;
    private Context mContext;
    private Gson mGson;

    private UserPresenter(Context context){
        mExecutorService = Executors.newCachedThreadPool();
        mDataBaseTools = new DataBaseTools(context);
        mContext = context;
        mGson = new Gson();
    }

    public void initUserData(){
        L.i(TAG, "initUserData");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                L.i(TAG, "create discusses");
                ArrayList<Data_DB_Discuss> discusses = new ArrayList<>();
                for(int i = 0;i<10;i++){
                    Data_DB_Discuss data = new Data_DB_Discuss();
                    data.setContent("屠呦呦宁波旧居要卖1.5亿 吐槽：现在满世界都是我");
                    data.setCreateTime(Method.getTS());
                    discusses.add(data);
                }
                DiscussEvent event = new DiscussEvent();
                event.setDiscusses(discusses);
                EventBus.getDefault().post(event);

                if(UserManager.getInstance(mContext).isLogined()){
                    UserEvent userEvent = new UserEvent();
                    userEvent.setAction(UserEvent.ACTION_GET_USERINFO);
                    userEvent.setExtra(UserManager.getInstance(mContext).getCurrentUser());
                    EventBus.getDefault().post(userEvent);
                }
            }
        });
    }

    public void register(final String username, final String email, final String password){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("loginId", email);
                params.put("email", email);
                params.put("password", password);
                httpPost httpPost = new httpPost();
                try {
                    String response = httpPost.requireClass(BHApplication.Address + "/user/register", params, "UTF-8");
                    L.i(TAG, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void login(final String username, final String password){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {

                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("loginId", username);
                params.put("password", password);
                httpPost httpPost = new httpPost();
                try {
                    String response = httpPost.requireClass(BHApplication.Address + "/user/login", params, "UTF-8");
                    L.i(TAG, response);
                    Response<Data_DB_User> userResponse = mGson.fromJson(response, )
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Data_DB_User user = new Data_DB_User();
//                user.setUserName(username);
//                user.setPassword(password);
//                mDataBaseTools.addData(Constants_DB.TABLE_USER, user);

//                if(UserManager.getInstance(mContext).login(username, password)){
//                    UserEvent event = new UserEvent();
//                    event.setAction(UserEvent.ACTION_LOGIN_SUCCESS);
//                    EventBus.getDefault().post(event);
//                }
            }
        });
    }

    public void saveUserInfo(final Data_DB_User user){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                String conditionStr = Constants_DB.USER_USERNAME + " = '" + user.getUserName() + "'";
                String valueStr = Constants_DB.USER_NICKNAME + " = '" + user.getNickName() + "',"
                        + Constants_DB.USER_LASTCHANGETIME + " = " + user.getLastChangeTime() + ","
                        + Constants_DB.USER_STATUS + " = '" + user.getStatus() + "',"
                        + Constants_DB.USER_PHOTOPATH + " = '" + user.getPhotoPath() + "'";
                if(mDataBaseTools.updateData(Constants_DB.TABLE_USER, conditionStr, valueStr)){
                    UserEvent event = new UserEvent();
                    event.setAction(UserEvent.ACTION_UPDATE_USERINFO_SUCCESS);
                    EventBus.getDefault().post(event);
                } else {
                    UserEvent event = new UserEvent();
                    event.setAction(UserEvent.ACTION_UPDATE_USERINFO_FAILED);
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    public void deleteUserInfo(){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                UserManager.getInstance(mContext).logout();
                UserEvent event = new UserEvent();
                event.setAction(UserEvent.ACTION_DELETE_USERINFO_COMPLETE);
                EventBus.getDefault().post(event);
            }
        });
    }

    private void pickPhotoFromGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
        activity.startActivityForResult(intent, requestCode);
    }
}
