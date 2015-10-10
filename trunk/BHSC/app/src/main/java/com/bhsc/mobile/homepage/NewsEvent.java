package com.bhsc.mobile.homepage;

import com.bhsc.mobile.datalcass.Data_DB_News;

import java.util.List;

/**
 * Created by lynn on 15-10-9.
 */
public class NewsEvent {
    private List<Data_DB_News> newsList;

    public List<Data_DB_News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<Data_DB_News> newsList) {
        this.newsList = newsList;
    }
}
