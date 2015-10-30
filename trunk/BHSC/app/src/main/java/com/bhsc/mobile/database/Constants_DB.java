package com.bhsc.mobile.database;

/**
 * Created by lynn on 10/20/15.
 */
public class Constants_DB {
    public static int DB_VERSION = 1;

    //爆料数据表
    public static String DB_NAME = "hbsc.db";

    //用户表
    public static String TABLE_USER = "table_user";
    public static String USER_USERNAME = "username";
    public static String USER_PASSWORD = "password";
    public static String USER_NICKNAME = "nickname";
    public static String USER_STATUS = "status";
    public static String USER_PHOTOPATH = "photo_path";
    public static String USER_LASTCHANGETIME = "last_change_time";

    public static String TABLE_DISCLOSE = "table_disclose";
    public static String DISCLOSE_USERNAME = "username";
    public static String DISCLOSE_CREATETIME = "create_time";
    public static String DISCLOSE_TITLE = "title";
    public static String DISCLOSE_CONTENT = "content";
    public static String DISCLOSE_IMAGEPATHS = "image_paths";
    public static String DISCLOSE_DATAID = "data_id";
}
