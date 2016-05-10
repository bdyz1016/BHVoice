package com.bhsc.mobile.disclose.model;

import com.bhsc.mobile.net.Response;

import java.util.List;

/**
 * Created by zhanglei on 16/4/6.
 */
public class DiscloseResponse extends Response{
    private List<Data_DB_Disclose> list;

    public List<Data_DB_Disclose> getList() {
        return list;
    }

    public void setList(List<Data_DB_Disclose> list) {
        this.list = list;
    }
}
