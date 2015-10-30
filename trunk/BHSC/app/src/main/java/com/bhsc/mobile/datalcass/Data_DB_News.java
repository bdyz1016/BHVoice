package com.bhsc.mobile.datalcass;

/**
 * Created by lynn on 15-9-17.
 */
public class Data_DB_News extends BaseClass{

    public static final int TYPE_NEWS = 0;
    public static final int TYPE_ADVERTISE = 1;

    /**
     * 0:新闻，1:广告
     */
    private int isAdv;
    private String Content = "";
    private String title = "";
    private String[] Images = null;
    private long PublishTime;
    private long createUserId;
    private String createTime;
    /**
     * 点赞数量
     */
    private int praiseCount;

    private String id = "";

    private String source = "";

    private int type;

    /**
     * 评论数量
     */
    private int commentCount;

    public Data_DB_News(){
        super();
    }

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int type) {
        isAdv = type;
    }

    public String getContent() {
        return Content;
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

    public void setContent(String content) {
        Content = content;
    }

    public void setTitle(String title) {
        title = title;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public void setPublishTime(long publishTime) {
        PublishTime = publishTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }
}
