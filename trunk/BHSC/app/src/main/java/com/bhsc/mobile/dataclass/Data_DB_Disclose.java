package com.bhsc.mobile.dataclass;

import java.util.List;

/**
 * Created by lynn on 15-9-30.
 */
public class Data_DB_Disclose extends BaseClass{
    /**
     * 图片最大数量
     */
    public static final int IMAGE_MAX_COUNT = 5;

    private String Content;
    private String Title;
    /**
     * 图片路径集合，图片路径之间用“,”分隔
     */
    private List<String> ImagePaths;
    private long createTime;
    private String UserName;
    /**
     * 点赞数
     */
    private int praiseCount;

    /**
     * 评论数
     */
    private int commentCount;

    private String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data_DB_Disclose(){
        super();
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public List<String> getImagePaths() {
        return ImagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        ImagePaths = imagePaths;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
