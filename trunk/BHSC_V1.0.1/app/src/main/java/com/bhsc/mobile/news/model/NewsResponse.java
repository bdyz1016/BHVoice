package com.bhsc.mobile.news.model;

import com.bhsc.mobile.net.Response;

import java.util.List;

/**
 * Created by lynn on 11/2/15.
 */
public class NewsResponse extends Response {
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

