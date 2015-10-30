package com.bhsc.mobile.manager;


import android.content.Context;
import android.database.Cursor;

import com.android.pc.ioc.internet.AjaxCallBack;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.datalcass.Data_DB_User;
import com.bhsc.mobile.main.BHApplication;
import com.bhsc.mobile.utils.L;

import java.util.LinkedHashMap;

/**
 * Created by lynn on 10/20/15.
 */
public class UserManager {
    private final String TAG = UserManager.class.getSimpleName();

    private static UserManager instance;

    public static UserManager getInstance(Context context){
        if(instance == null){
            synchronized (UserManager.class){
                if(instance == null){
                    instance = new UserManager(context);
                }
            }
        }
        return instance;
    }

    private boolean isLogined = false;
    private Data_DB_User mCurrentUser;

    private DataBaseTools mDataBaseTools;
    private UserManager(Context context){
        mDataBaseTools = new DataBaseTools(context);
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }

    public Data_DB_User getCurrentUser() {
        if(mCurrentUser == null){
            synchronized (UserManager.class){
                if(mCurrentUser == null){
                    Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_USER, null, null);
                    if(cursor != null && cursor.getCount() > 0){
                        cursor.moveToNext();
                        mCurrentUser= new Data_DB_User();
                        mCurrentUser.setPassword(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_PASSWORD)));
                        mCurrentUser.setNickName(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_NICKNAME)));
                        mCurrentUser.setUserName(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_USERNAME)));
                        mCurrentUser.setStatus(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_STATUS)));
                        mCurrentUser.setPhotoPath(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_PHOTOPATH)));
                        mCurrentUser.setLastChangeTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.USER_LASTCHANGETIME)));
                        this.isLogined = true;
                    }
                }
            }
        }
        return mCurrentUser;
    }

    public void setCurrentUser(Data_DB_User currentUser) {
        this.mCurrentUser = currentUser;
    }

    public boolean login(String username, String password){
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("loginId", username);
        params.put("password", password);
        FastHttp.ajaxForm(BHApplication.Address + "/user/login", params, new AjaxCallBack() {
            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void callBack(ResponseEntity responseEntity) {
                L.i(TAG, responseEntity.toString());
            }
        });
        Data_DB_User user = new Data_DB_User();
        user.setUserName(username);
        user.setPassword(password);
        boolean result = false;
        result = mDataBaseTools.addData(Constants_DB.TABLE_USER, user);
        if(result){
            isLogined = true;
        }
        return result;
    }

    public boolean logout(){
        boolean result = false;
        result = mDataBaseTools.deleteData(Constants_DB.TABLE_USER, null);
        if(result){
            isLogined = false;
        }
        return result;
    }
}
