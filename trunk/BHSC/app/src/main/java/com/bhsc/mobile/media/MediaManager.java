package com.bhsc.mobile.media;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.dataclass.Data_DB_Picture;

import java.util.ArrayList;

/**
 * Created by lynn on 11/4/15.
 */
public class MediaManager {

    private DataBaseTools mDataBaseTools;

    public MediaManager(Context context){
        mDataBaseTools = new DataBaseTools(context);
    }

    public ArrayList<String> getPicturePaths(String id){
        ArrayList<String> paths = new ArrayList<>();
        String conditionStr = Constants_DB.PICTURE_ID + " = '" + id + "'";
        Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_PICTURE, new String[]{Constants_DB.PICTURE_PATH}, conditionStr);
        if(cursor!=null){
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                paths.add(cursor.getString(cursor.getColumnIndex(Constants_DB.PICTURE_PATH)));
            }
            cursor.close();
        }
        return paths;
    }

    public boolean savePicturePath(Data_DB_Picture picture){
        return mDataBaseTools.addData(Constants_DB.TABLE_PICTURE, picture);
    }

    public boolean deletePicturePath(String id){
        if(TextUtils.isEmpty(id)){
            return false;
        }
        return mDataBaseTools.deleteData(Constants_DB.TABLE_PICTURE, id);
    }
}
