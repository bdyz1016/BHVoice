package com.bhsc.mobile.news.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

/**
 * Created by zhanglei on 16/5/31.
 */
public class Data_DB_Detail extends SugarRecord implements Parcelable{

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


    public static final Parcelable.Creator<Data_DB_Detail> CREATOR = new Creator<Data_DB_Detail>() {
        @Override
        public Data_DB_Detail createFromParcel(Parcel source) {
            return new Data_DB_Detail(source);
        }

        @Override
        public Data_DB_Detail[] newArray(int size) {
            return new Data_DB_Detail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentCount);
        dest.writeString(content);
        dest.writeLong(createTime);
        dest.writeLong(createUserId);
        dest.writeInt(isAdv);
        dest.writeString(title);
        dest.writeLong(PublishTime);
        dest.writeString(titleImg);
        dest.writeInt(praiseCount);
        dest.writeString(source);
        dest.writeInt(type);
        dest.writeString(shareurl);
    }

    private Data_DB_Detail(Parcel source){
        this.commentCount = source.readInt();
        this.content = source.readString();
        this.createTime = source.readLong();
        this.createUserId = source.readLong();
        this.isAdv = source.readInt();
        this.title = source.readString();
        this.PublishTime = source.readLong();
        this.titleImg = source.readString();
        this.praiseCount = source.readInt();
        this.source = source.readString();
        this.type = source.readInt();
        this.shareurl = source.readString();
    }
}
