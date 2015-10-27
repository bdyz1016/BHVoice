package com.bhsc.mobile.manager;


import com.bhsc.mobile.datalcass.Data_DB_User;

/**
 * Created by lynn on 10/20/15.
 */
public class UserManager {

    private static class InstanceHolder{
        public static UserManager instance = new UserManager();
    }

    public static UserManager getInstance(){
        return InstanceHolder.instance;
    }

    private boolean isLogined = false;
    private Data_DB_User mCurrentUser;

    private UserManager(){
        mCurrentUser = new Data_DB_User();
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }

    public Data_DB_User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(Data_DB_User currentUser) {
        this.mCurrentUser = currentUser;
    }
}
