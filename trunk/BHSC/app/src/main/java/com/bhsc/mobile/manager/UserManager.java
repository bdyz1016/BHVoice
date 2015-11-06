package com.bhsc.mobile.manager;


import android.content.Context;
import android.database.Cursor;

import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.dataclass.Data_DB_User;

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
                        mCurrentUser.setUsername(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_USERNAME)));
                        mCurrentUser.setStatus(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_STATUS)));
                        mCurrentUser.setHeadurl(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_PHOTOPATH)));
                        mCurrentUser.setLastChangeTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.USER_LASTCHANGETIME)));
                        mCurrentUser.setEmail(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_EMAIL)));
                        mCurrentUser.setUserId(cursor.getString(cursor.getColumnIndex(Constants_DB.USER_USERID)));
                        this.isLogined = true;
                        cursor.close();
                    }
                }
            }
        }
        return mCurrentUser;
    }

    public synchronized boolean updateCurrentUser(Data_DB_User user){
        boolean result = false;
        String conditionStr = Constants_DB.USER_USERNAME + " = '" + user.getUsername() + "'";
        String valueStr = Constants_DB.USER_NICKNAME + " = '" + user.getNickName() + "',"
                + Constants_DB.USER_LASTCHANGETIME + " = " + user.getLastChangeTime() + ","
                + Constants_DB.USER_STATUS + " = '" + user.getStatus() + "',"
                + Constants_DB.USER_PHOTOPATH + " = '" + user.getHeadurl() + "'";
        result = mDataBaseTools.updateData(Constants_DB.TABLE_USER, conditionStr, valueStr);
        if(result){
            getCurrentUser();
        }
        return result;
    }

    public void setCurrentUser(Data_DB_User currentUser) {
        this.mCurrentUser = currentUser;
    }

    public boolean login(Data_DB_User user){
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
