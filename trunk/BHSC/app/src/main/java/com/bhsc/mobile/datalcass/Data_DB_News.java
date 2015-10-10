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
    private int Type;
    private String Content = "";
    private String Title = "";
    private String[] Images = null;
    private long PublishTime;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getContent() {
        return Content;
    }

    public String getTitle() {
        return Title;
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
        Title = title;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public void setPublishTime(long publishTime) {
        PublishTime = publishTime;
    }
}
