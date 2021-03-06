
package com.bhsc.mobile.database;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhsc.mobile.dataclass.Data_DB_Disclose;
import com.bhsc.mobile.dataclass.Data_DB_News;
import com.bhsc.mobile.dataclass.Data_DB_Picture;
import com.bhsc.mobile.dataclass.Data_DB_User;


public class DataBaseTools {

    private static String TAG = "DataBaseTools";
    private SQLiteDatabase db;

    public DataBaseTools(Context context) {
        db = DataBaseOpenHelper.getInstance(context);
    }


    public Boolean deleteData(String tableName, String conditionStr) {
        Boolean iResult = false;
        String sql = "";
        if (conditionStr != null && conditionStr.length() > 0) {
            sql = "DELETE FROM  " + tableName + " where " + conditionStr;
        } else {
            sql = "DELETE FROM  " + tableName;
        }
        db.beginTransaction();
        try {
            db.execSQL(sql);
            iResult = true;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            iResult = false;
        } finally {
            db.endTransaction();
        }
        return iResult;
    }

    public Boolean deleteData(String tableName) {
        Boolean isResult = false;

        String sql = "";
        sql = "DELETE FROM  " + tableName;
        db.beginTransaction();
        try {
            this.db.execSQL(sql);
            isResult = true;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            isResult = false;
        } finally {
            db.endTransaction();
        }
        return isResult;
    }


    public Boolean deleteAllTableData() {
        Boolean isResult = false;

        Cursor cursor = db
                .rawQuery(
                        "select name from sqlite_master where type='table' order by name",
                        null);
        while (cursor.moveToNext()) {
            // 遍历出表名
            String name = cursor.getString(0);
            String sql = "DELETE FROM  " + name;
            Log.i(TAG, "删除所有表的数据name：" + name);
            Log.i(TAG, "删除所有表的数据sql：" + sql);
            db.beginTransaction();
            try {
                this.db.execSQL(sql);
                isResult = true;
                db.setTransactionSuccessful();
            } catch (SQLException e) {
                e.printStackTrace();
                isResult = false;
                Log.e(TAG, "删除表的数据SQLException:" + name);
            } finally {
                db.endTransaction();
            }
        }
        cursor.close();
        return isResult;
    }

    public Cursor selectData(String tableName, String columns[],
                             String conditionStr) {
        Cursor cur = null;

        db.beginTransaction();
        try {
            cur = this.db.query(tableName, columns, conditionStr, null, null,
                    null, null);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cur;
    }

    public Cursor selectDataOrderBy(String tableName, String columns[],
                                    String conditionStr, String orderBy) {
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = this.db.query(tableName, columns, conditionStr, null, null,
                    null, orderBy);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cur;
    }


    public Boolean updateData(String tableName, String conditionStr,
                              String valueStr) {
        Log.i(tableName, "tableName:" + tableName);
        Boolean isResult = false;
        String sql = "";
        if (conditionStr.length() > 0) {
            sql = "UPDATE " + tableName + " SET " + valueStr + " where "
                    + conditionStr + ";";
        } else {
            sql = "UPDATE " + tableName + " SET " + valueStr;
        }
        Log.i(TAG, "sql:" + sql);
        db.beginTransaction();
        try {
            this.db.execSQL(sql);
            isResult = true;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Update sql 进入Catch");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            // System.out.println("*****="+sw.getBuffer().toString());
            Log.e(TAG, "catch = " + sw.getBuffer().toString());
            e.printStackTrace();
            isResult = false;
        } finally {
            db.endTransaction();
        }
        return isResult;
    }

    public boolean addData(String tableName, Object obj) {

        boolean isResult = false;
        String sql = "";

        if (tableName.equals(Constants_DB.TABLE_USER)) {
            sql = "insert into " + Constants_DB.TABLE_USER + " ("
                    + Constants_DB.USER_USERNAME + ","
                    + Constants_DB.USER_PASSWORD + ","
                    + Constants_DB.USER_LASTCHANGETIME + ","
                    + Constants_DB.USER_NICKNAME + ","
                    + Constants_DB.USER_STATUS + ","
                    + Constants_DB.USER_PHOTOPATH + ","
                    + Constants_DB.USER_EMAIL + ","
                    + Constants_DB.USER_USERID
                    + ")VALUES('"
                    + ((Data_DB_User) obj).getUsername() + "','"
                    + ((Data_DB_User) obj).getPassword() + "',"
                    + ((Data_DB_User) obj).getLastChangeTime() + ",'"
                    + ((Data_DB_User) obj).getNickName() + "','"
                    + ((Data_DB_User) obj).getStatus() + "','"
                    + ((Data_DB_User) obj).getHeadurl() + "','"
                    + ((Data_DB_User) obj).getEmail() + "','"
                    + ((Data_DB_User) obj).getUserId()
                    + "');";
        } else if (tableName.equals(Constants_DB.TABLE_DISCLOSE)) {
            sql = "insert into " + Constants_DB.TABLE_DISCLOSE + " ("
                    + Constants_DB.DISCLOSE_CONTENT + ","
                    + Constants_DB.DISCLOSE_CREATETIME + ","
                    + Constants_DB.DISCLOSE_TITLE + ","
                    + Constants_DB.DISCLOSE_DATAID + ","
                    + Constants_DB.DISCLOSE_USERNAME + ","
                    + Constants_DB.DISCLOSE_COMMENTCOUNT + ","
                    + Constants_DB.DISCLOSE_PRAISECOUNT
                    + ""
                    + ")VALUES('"
                    + ((Data_DB_Disclose) obj).getContent() + "',"
                    + ((Data_DB_Disclose) obj).getCreateTime() + ",'"
                    + ((Data_DB_Disclose) obj).getTitle() + "','"
                    + ((Data_DB_Disclose) obj).getId() + "','"
                    + ((Data_DB_Disclose) obj).getUserName() + "',"
                    + ((Data_DB_Disclose) obj).getCommentCount() + ","
                    + ((Data_DB_Disclose) obj).getPraiseCount()
                    + ");";
        } else if (tableName.equals(Constants_DB.TABLE_PICTURE)) {
            sql = "insert into " + Constants_DB.TABLE_PICTURE + " ("
                    + Constants_DB.PICTURE_ID + ","
                    + Constants_DB.PICTURE_PATH
                    + ")VALUES('"
                    + ((Data_DB_Picture) obj).getId() + "','"
                    + ((Data_DB_Picture) obj).getPath() +  "');";
        } else if (tableName.equals(Constants_DB.TABLE_NEWS)) {
            sql = "insert into " + Constants_DB.TABLE_NEWS + " ("
                    + Constants_DB.NEWS_ISADV + ","
                    + Constants_DB.NEWS_CONTENT + ","
                    + Constants_DB.NEWS_TITLE + ","
                    + Constants_DB.NEWS_PUBLISHTIME + ","
                    + Constants_DB.NEWS_CREATEUSERID + ","
                    + Constants_DB.NEWS_CREATETIME + ","
                    + Constants_DB.NEWS_PRAISECOUNT + ","
                    + Constants_DB.NEWS_ID + ","
                    + Constants_DB.NEWS_SOURCE + ","
                    + Constants_DB.NEWS_TYPE + ","
                    + Constants_DB.NEWS_COMMENTCOUNT
                    + ")VALUES("
                    + ((Data_DB_News) obj).getIsAdv() + ",'"
                    + ((Data_DB_News) obj).getContent() + "','"
                    + ((Data_DB_News) obj).getTitle() + "',"
                    + ((Data_DB_News) obj).getPublishTime() + ","
                    + ((Data_DB_News) obj).getCreateUserId() + ",'"
                    + ((Data_DB_News) obj).getCreateTime() + "',"
                    + ((Data_DB_News) obj).getPraiseCount() + ",'"
                    + ((Data_DB_News) obj).getId() + "','"
                    + ((Data_DB_News) obj).getSource() + "',"
                    + ((Data_DB_News) obj).getType() + ","
                    + ((Data_DB_News) obj).getCommentCount()
                    +  ");";
        }

        db.beginTransaction();
        try {
            this.db.execSQL(sql);
            isResult = true;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            isResult = false;
        } finally {
            db.endTransaction();
        }
        return isResult;
    }
}