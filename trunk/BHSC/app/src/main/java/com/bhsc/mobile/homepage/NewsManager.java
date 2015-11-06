package com.bhsc.mobile.homepage;

import android.content.Context;
import android.database.Cursor;

import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.dataclass.Data_DB_News;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lynn on 11/5/15.
 */
public class NewsManager {

    private DataBaseTools mDataBaseTools;

    public NewsManager(Context context) {
        mDataBaseTools = new DataBaseTools(context);
    }

    public void saveNews(List<Data_DB_News> newsList) {
        for (Data_DB_News news : newsList) {
            mDataBaseTools.addData(Constants_DB.TABLE_NEWS, news);
        }
    }

    public LinkedList<Data_DB_News> getAllNews() {
        LinkedList<Data_DB_News> newsList = new LinkedList<>();
        Cursor cursor = mDataBaseTools.selectDataOrderBy(Constants_DB.TABLE_NEWS, null, null, "DESC");
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Data_DB_News news = new Data_DB_News();
                news.setIsAdv(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_ISADV)));
                news.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_PRAISECOUNT)));
                news.setCommentCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_COMMENTCOUNT)));
                news.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CONTENT)));
                news.setCreateTime(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CREATETIME)));
                news.setCreateUserId(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_CREATEUSERID)));
                news.setId(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_ID)));
                news.setPublishTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_PUBLISHTIME)));
                news.setSource(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_SOURCE)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_TITLE)));
                news.setType(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_TYPE)));
                newsList.add(news);
            }
            cursor.close();
        }
        return newsList;
    }

    public LinkedList<Data_DB_News> getAllNews(int type) {
        LinkedList<Data_DB_News> newsList = new LinkedList<>();
        String conditionStr = Constants_DB.NEWS_TYPE + " = " + type;
        Cursor cursor = mDataBaseTools.selectDataOrderBy(Constants_DB.TABLE_NEWS, null, conditionStr, "DESC");
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Data_DB_News news = new Data_DB_News();
                news.setIsAdv(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_ISADV)));
                news.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_PRAISECOUNT)));
                news.setCommentCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_COMMENTCOUNT)));
                news.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CONTENT)));
                news.setCreateTime(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CREATETIME)));
                news.setCreateUserId(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_CREATEUSERID)));
                news.setId(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_ID)));
                news.setPublishTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_PUBLISHTIME)));
                news.setSource(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_SOURCE)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_TITLE)));
                news.setType(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_TYPE)));
                newsList.add(news);
            }
            cursor.close();
        }
        return newsList;
    }

    public Data_DB_News getNews(String id) {
        Data_DB_News news = null;
        String conditionStr = Constants_DB.NEWS_ID + " = '" + id + "'";
        Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_NEWS, null, conditionStr);
        if (cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                news = new Data_DB_News();
                news.setIsAdv(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_ISADV)));
                news.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_PRAISECOUNT)));
                news.setCommentCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_COMMENTCOUNT)));
                news.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CONTENT)));
                news.setCreateTime(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_CREATETIME)));
                news.setCreateUserId(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_CREATEUSERID)));
                news.setId(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_ID)));
                news.setPublishTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.NEWS_PUBLISHTIME)));
                news.setSource(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_SOURCE)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.NEWS_TITLE)));
                news.setType(cursor.getInt(cursor.getColumnIndex(Constants_DB.NEWS_TYPE)));
            }
            cursor.close();
        }
        return news;
    }
}
