package com.bhsc.mobile.disclose;

import android.content.Context;
import android.database.Cursor;

import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.dataclass.Data_DB_Disclose;
import com.bhsc.mobile.media.MediaManager;

import java.util.ArrayList;

/**
 * Created by lynn on 11/4/15.
 */
public class DiscloseManager {

    private DataBaseTools mDataBaseTools;
    private MediaManager mMediaManager;

    public DiscloseManager(Context context){
        mDataBaseTools = new DataBaseTools(context);
        mMediaManager = new MediaManager(context);
    }

    public ArrayList<Data_DB_Disclose> getAllDisclose(){
        Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_DISCLOSE, null, null);
        ArrayList<Data_DB_Disclose> discloses = new ArrayList<Data_DB_Disclose>();
        if(cursor != null){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                Data_DB_Disclose disclose = new Data_DB_Disclose();
                disclose.setUserName(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_USERNAME)));
                disclose.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_CONTENT)));
                disclose.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.DISCLOSE_CREATETIME)));
                disclose.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_TITLE)));
                disclose.setId(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_DATAID)));
                disclose.setCommentCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.DISCLOSE_COMMENTCOUNT)));
                disclose.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.DISCLOSE_PRAISECOUNT)));
                disclose.setImagePaths(mMediaManager.getPicturePaths(disclose.getId()));
                discloses.add(disclose);
            }
            cursor.close();
        }
        return discloses;
    }

    public Data_DB_Disclose getDisclose(String dataId){
        Data_DB_Disclose disclose = new Data_DB_Disclose();
        String conditionStr = Constants_DB.DISCLOSE_DATAID + " = '" + dataId + "'";
        Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_DISCLOSE, null, conditionStr);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            disclose.setUserName(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_USERNAME)));
            disclose.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_CONTENT)));
            disclose.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.DISCLOSE_CREATETIME)));
            disclose.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_TITLE)));
            disclose.setId(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_DATAID)));
            disclose.setCommentCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.DISCLOSE_COMMENTCOUNT)));
            disclose.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants_DB.DISCLOSE_PRAISECOUNT)));
            disclose.setImagePaths(mMediaManager.getPicturePaths(disclose.getId()));
            cursor.close();
        }
        return disclose;
    }

    public boolean deleteDisclose(Data_DB_Disclose disclose){
        boolean result = false;
        String conditionStr = Constants_DB.DISCLOSE_DATAID + " = '" + disclose.getId() + "'";
        if(mDataBaseTools.deleteData(Constants_DB.TABLE_DISCLOSE, conditionStr)) {//删除爆料
            result = mMediaManager.deletePicturePath(disclose.getId());//删除图片
        }
        return result;
    }

    public boolean updataDisclose(Data_DB_Disclose disclose){
        String conditionStr = Constants_DB.DISCLOSE_DATAID + " = '" + disclose.getId() + "'";
        String valueStr = Constants_DB.DISCLOSE_COMMENTCOUNT + " = " + disclose.getCommentCount() + ","
                + Constants_DB.DISCLOSE_PRAISECOUNT + " = " + disclose.getPraiseCount() + ","
                + Constants_DB.DISCLOSE_CONTENT + " = '" + disclose.getContent() + "',"
                + Constants_DB.DISCLOSE_TITLE + " = '" + disclose.getTitle() + "'";
        return mDataBaseTools.updateData(Constants_DB.TABLE_DISCLOSE, conditionStr, valueStr);
    }
}
