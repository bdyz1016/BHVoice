package com.bhsc.mobile.news.model;

import com.bhsc.mobile.net.Response;

/**
 * Created by zhanglei on 16/5/31.
 */
public class DetailResponse extends Response {
    private Data_DB_Detail news;

    public Data_DB_Detail getNews() {
        return news;
    }

    public void setNews(Data_DB_Detail news) {
        this.news = news;
    }
}
