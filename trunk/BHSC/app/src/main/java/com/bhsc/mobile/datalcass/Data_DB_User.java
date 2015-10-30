package com.bhsc.mobile.datalcass;

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

    private String UserName;
    private String Password;
    private String Status;
    private String NickName;
    private String PhotoPath;
    private long LastChangeTime;
    /**
     * 性别  0：不确定，1：男 ，2：女
     */
    private int Gender;

    private String id = "";

    public Data_DB_User(){
        super();
        this.UserName = "";
        this.Password = "";
        this.Status = "";
        this.NickName = "";
        this.PhotoPath = "";
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastChangeTime() {
        return LastChangeTime;
    }

    public void setLastChangeTime(long lastChangeTime) {
        LastChangeTime = lastChangeTime;
    }
}
