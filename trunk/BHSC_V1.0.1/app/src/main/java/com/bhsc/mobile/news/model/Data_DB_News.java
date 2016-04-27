package com.bhsc.mobile.news.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

/**
 * Created by lynn on 15-9-17.
 */
public class Data_DB_News extends SugarRecord {

    public static final int TYPE_NEWS = 0;
    public static final int TYPE_ADVERTISE = 1;

    /**
     * 0:新闻，1:广告
     */
    private int isAdv;
    private String content = "";
    private String title = "";
    private String[] Images = null;
    @Column(name="publish_time")
    private long PublishTime;
    private long createUserId;
    @Column(name="createTime")
    private long createTime;
    private String titleImg;
    /**
     * 点赞数量
     */
    private int praiseCount;

    private String source = "";

    private int type;

    /**
     * 评论数量
     */
    private int commentCount;

    private int isShow;

    public Data_DB_News() {
        super();
    }

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int type) {
        isAdv = type;
    }

    public String getTitle() {
        return title;
    }

    public String[] getImages() {
        return Images;
    }

    public long getPublishTime() {
        return PublishTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public void setPublishTime(long publishTime) {
        PublishTime = publishTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
