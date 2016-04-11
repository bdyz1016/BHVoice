package com.bhsc.disclose.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import java.util.List;

/**
 * Created by lynn on 15-9-30.
 */
public class Data_DB_Disclose extends SugarRecord {
    /**
     * 图片最大数量
     */
    public static final int IMAGE_MAX_COUNT = 9;

    private String imgs;
    @Column(name="create_time")
    private long createTime;
    private String headurl;
    private int state;
    private String title;
    private int userId;
    private String content;
    private String username;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
