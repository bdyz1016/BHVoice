package com.bhsc.mobile.net;

import com.bhsc.mobile.datalcass.Data_DB_News;

import java.util.List;

/**
 * Created by lynn on 10/28/15.
 */
public class NewsResponse {
    private int code;
    private List<Data_DB_News> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Data_DB_News> getList() {
        return list;
    }

    public void setList(List<Data_DB_News> list) {
        this.list = list;
    }
}
