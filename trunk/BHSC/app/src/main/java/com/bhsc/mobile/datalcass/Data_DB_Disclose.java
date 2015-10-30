package com.bhsc.mobile.datalcass;

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
    private String ImagePaths;
    private long createTime;
    private String UserName;

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

    public String getImagePaths() {
        return ImagePaths;
    }

    public void setImagePaths(String imagePaths) {
        ImagePaths = imagePaths;
    }
}
