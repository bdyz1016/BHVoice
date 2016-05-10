package com.bhsc.mobile.ThirdParty.model;

/**
 * Created by zhanglei on 16/5/3.
 */
public class WechatLoginResponse {
    private WechatUserInfo user_data;
    private WechatVerify verify_data;

    public WechatUserInfo getUser_data() {
        return user_data;
    }

    public void setUser_data(WechatUserInfo user_data) {
        this.user_data = user_data;
    }

    public WechatVerify getVerify_data() {
        return verify_data;
    }

    public void setVerify_data(WechatVerify verify_data) {
        this.verify_data = verify_data;
    }
}
