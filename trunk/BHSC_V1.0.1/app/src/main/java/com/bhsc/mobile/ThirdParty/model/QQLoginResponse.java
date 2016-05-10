package com.bhsc.mobile.ThirdParty.model;

/**
 * Created by zhanglei on 16/5/3.
 */
public class QQLoginResponse {
    private QQUserInfo user_data;
    private QQVerify verify_data;

    public QQUserInfo getUser_data() {
        return user_data;
    }

    public void setUser_data(QQUserInfo user_data) {
        this.user_data = user_data;
    }

    public QQVerify getVerify_data() {
        return verify_data;
    }

    public void setVerify_data(QQVerify verify_data) {
        this.verify_data = verify_data;
    }
}
