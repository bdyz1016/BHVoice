package com.bhsc.disclose.model;

import com.bhsc.net.Response;

import java.util.List;

/**
 * Created by zhanglei on 16/4/6.
 */
public class DiscloseResponse extends Response{
    private int code;
    private List<Data_DB_Disclose> list;

    public List<Data_DB_Disclose> getList() {
        return list;
    }

    public void setList(List<Data_DB_Disclose> list) {
        this.list = list;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
