package com.bhsc.mobile.news.event;


import com.bhsc.mobile.news.model.Data_DB_News;

import java.util.List;

/**
 * Created by lynn on 15-10-9.
 */
public class NewsEvent {
    public static final int ACTION_REFRESH_COMPLETE = 0X1;
    public static final int ACTION_REFRESH_ENABLE = 0X2;
    public static final int ACTION_REFRESH_DISABLE = 0X3;

    private int Action;
    private int NewsType;
    private List<Data_DB_News> newsList;

    public List<Data_DB_News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<Data_DB_News> newsList) {
        this.newsList = newsList;
    }

    public int getAction() {
        return Action;
    }

    public void setAction(int action) {
        Action = action;
    }

    public int getNewsType() {
        return NewsType;
    }

    public void setNewsType(int newsType) {
        NewsType = newsType;
    }
}
