package com.bhsc.mobile.homepage.newsdetail;

import com.bhsc.mobile.dataclass.Data_DB_News;

import java.util.List;

/**
 * Created by lynn on 15-10-9.
 */
public class RelatedNewsEvent {
    private List<Data_DB_News> newsList;

    public List<Data_DB_News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<Data_DB_News> newsList) {
        this.newsList = newsList;
    }
}
