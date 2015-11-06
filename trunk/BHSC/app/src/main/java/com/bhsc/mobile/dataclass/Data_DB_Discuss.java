package com.bhsc.mobile.dataclass;

/**
 * Created by lynn on 15-10-8.
 */
public class Data_DB_Discuss extends BaseClass{
    private String content = "";
    private long createTime;
    /**
     * 评论同步到用户状态
     */
    private boolean ToUserStatus = false;

    private String id = "";

    private String fatherId = "";

    private String userId = "";

    private int isFreeze;

    private int type;

    public Data_DB_Discuss(){
        super();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isToUserStatus() {
        return ToUserStatus;
    }

    public void setToUserStatus(boolean toUserStatus) {
        ToUserStatus = toUserStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(int isFreeze) {
        this.isFreeze = isFreeze;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
