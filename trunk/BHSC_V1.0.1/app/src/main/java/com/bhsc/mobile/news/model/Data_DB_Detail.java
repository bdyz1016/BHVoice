package com.bhsc.mobile.news.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

/**
 * Created by zhanglei on 16/5/31.
 */
public class Data_DB_Detail extends SugarRecord {

    /**
     * 0:新闻，1:广告
     */
    private int isAdv;
    private String content = "";
    private String title = "";
    @Column(name="publish_time")
    private long PublishTime;
    private long createUserId;
    @Column(name="createTime")
    private long createTime;
    private String titleImg;
    private int praiseCount;
    private String source;
    private int type;
    private int commentCount;
    private String shareurl = "";

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int isAdv) {
        this.isAdv = isAdv;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(long publishTime) {
        PublishTime = publishTime;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }
}
