package com.bhsc.userpages;

import com.bhsc.userpages.model.Data_DB_User;
import com.orm.SugarRecord;

/**
 * Created by zhanglei on 16/3/31.
 */
public class UserManager {
    public static final int ERROR_USERNAME_EMPTY = 0X1;
    public static final int ERROR_USERNAME_FORMAT = 0X2;
    public static final int ERROR_PASSWORD_EMPTY = 0X3;
    public static final int ERROR_PASSWORD_FORMAT = 0X4;
    public static final int ERROR_USERNAME_WRONG = 0X5;
    public static final int ERROR_PASSWORD_WRONG = 0X6;
    public static final int ERROR_PASSWORD_INCONSISTENT = 0X7;
    public static final int ERROR_USERNAME_EXIST = 0X8;
    public static final int ERROR_UNKNOWN = 0X20;

    public static synchronized boolean isLogin(){
        return SugarRecord.count(Data_DB_User.class) > 0;
    }

    public static synchronized void login(Data_DB_User user){
        if(user == null){
            return;
        }
        user.save();
    }

    public static synchronized boolean logout(){
        return SugarRecord.deleteAll(Data_DB_User.class) > 0;
    }

    public static synchronized Data_DB_User getUser(){
        return SugarRecord.first(Data_DB_User.class);
    }
}
