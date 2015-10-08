package com.bhsc.mobile.datalcass;

/**
 * Created by lynn on 15-10-8.
 */
public class Data_DB_Discuss extends BaseClass{
    private String Content = "";
    private long createTime;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
