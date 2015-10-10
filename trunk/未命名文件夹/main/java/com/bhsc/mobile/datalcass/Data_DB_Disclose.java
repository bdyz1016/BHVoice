package com.bhsc.mobile.datalcass;

/**
 * Created by lynn on 15-9-30.
 */
public class Data_DB_Disclose extends BaseClass{
    private String Content;
    private String Title;
    private String[] Images;
    private String createTime;

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

    public String[] getImages() {
        return Images;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
