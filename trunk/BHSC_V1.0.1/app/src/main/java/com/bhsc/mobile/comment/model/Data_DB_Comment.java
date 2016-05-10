package com.bhsc.mobile.comment.model;

import android.support.annotation.NonNull;

import com.orm.SugarRecord;

/**
 * Created by lynn on 15-10-8.
 */
public class Data_DB_Comment extends SugarRecord implements Comparable<Data_DB_Comment> {

    private int isFreeze;
    private int type;
    private long userId;
    private long fatherId;
    private long time;
    private String headurl;
    private String content;
    private String username;

    public int getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(int isFreeze) {
        this.isFreeze = isFreeze;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(long fatherId) {
        this.fatherId = fatherId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(@NonNull Data_DB_Comment another) {
        if(time > another.getTime()){
            return -1;
        } else if(time < another.getTime()){
            return 1;
        } else {
            return 0;
        }
    }
}
