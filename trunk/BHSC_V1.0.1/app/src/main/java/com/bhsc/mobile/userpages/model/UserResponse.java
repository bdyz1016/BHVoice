package com.bhsc.mobile.userpages.model;

import com.bhsc.mobile.net.Response;

/**
 * Created by lynn on 11/3/15.
 */
public class UserResponse extends Response {
    private Data_DB_User user;

    public Data_DB_User getUser() {
        return user;
    }

    public void setUser(Data_DB_User user) {
        this.user = user;
    }
}
