package com.bhsc.mobile.dataclass;

/**
 * Created by lynn on 10/20/15.
 */
public class Data_DB_User extends BaseClass{
    /**
     * 性别：男
     */
    public static final int GENDER_MALE = 1;
    /**
     * 性别：女
     */
    public static final int GENDER_FEMALE = 2;
    /**
     * 性别：不确定
     */
    public static final int GENDER_UNCERTAIN = 0;

    private String username;
    private String Password;
    private String email;
    private String Status;
    private String NickName;
    private String headurl;
    private long LastChangeTime;
    private int isAdmin;
    private int isFreeze;
    private String createTime;
    private String id;
    /**
     * 性别  0：不确定，1：男 ，2：女
     */
    private int Gender;

    public Data_DB_User(){
        super();
        this.username = "";
        this.Password = "";
        this.Status = "";
        this.NickName = "";
        this.email = "";
        this.headurl = "";
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public long getLastChangeTime() {
        return LastChangeTime;
    }

    public void setLastChangeTime(long lastChangeTime) {
        LastChangeTime = lastChangeTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(int isFreeze) {
        this.isFreeze = isFreeze;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }
}
