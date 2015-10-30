package com.bhsc.mobile.disclose;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.datalcass.Data_DB_Disclose;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.media.FileUtil;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.utils.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 10/15/15.
 */
public class DisclosePresenter {

    private ExecutorService mExecutorService;

    private DataBaseTools mDataBaseTools;

    private static DisclosePresenter sDisclosePresenter;

    public static DisclosePresenter getInstance(Context context){
        if(sDisclosePresenter == null){
            synchronized (DisclosePresenter.class){
                if(sDisclosePresenter == null){
                    sDisclosePresenter = new DisclosePresenter(context);
                }
            }
        }
        return sDisclosePresenter;
    }

    private DisclosePresenter(Context context){
        mExecutorService = Executors.newCachedThreadPool();
        mDataBaseTools = new DataBaseTools(context);
    }

    private final int DEFAULT_QUALITY = 50;

    /**
     * 保存图片
     * @param bmp
     * @return 图片名
     */
    public String savePicture(final Bitmap bmp,String absolutePath){
        String imageName = "";
        try {
            imageName = ImageUtil.getInstance().savePicture(absolutePath, bmp, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageName;
    }

    /**
     * 拷贝图片到指定目录
     * @param source
     * @return 成功返回图片绝对路径，失败返回null
     */
    public String copyPicture(Context context, Uri source, String dest){
        if(ImageUtil.getInstance().copyPicture(context, source, dest)){
            return dest;
        }
        return null;
    }

    public void publish(final Data_DB_Disclose disclose, final List<String> picturePaths){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                StringBuilder pathBuilder = new StringBuilder();
                if(picturePaths != null){
                    for(int i = 0;i < picturePaths.size();i++){
                        String path = FileUtil.move(picturePaths.get(i), ImageUtil.ImagePath + Method.getTS() + ".png");
                        if(i > 0){
                            pathBuilder.append(',' + path);
                        } else {
                            pathBuilder.append(path);
                        }
                    }
                }
                disclose.setImagePaths(pathBuilder.toString());
                mDataBaseTools.addData(Constants_DB.TABLE_DISCLOSE, disclose);
                EventBus.getDefault().post(new ActionEvent(ActionEvent.ACTION_ADD_DISCLOSE_FINISH));
            }
        });
    }

    public void getAllDisclose(){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                ActionEvent event = new ActionEvent(ActionEvent.ACTION_LOAD_DISCLOSE);
                Cursor cursor = mDataBaseTools.selectData(Constants_DB.TABLE_DISCLOSE, null, null);
                ArrayList<Data_DB_Disclose> discloses = new ArrayList<Data_DB_Disclose>();
                if(cursor != null){
                    for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                        Data_DB_Disclose disclose = new Data_DB_Disclose();
                        disclose.setImagePaths(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_IMAGEPATHS)));
                        disclose.setUserName(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_USERNAME)));
                        disclose.setContent(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_CONTENT)));
                        disclose.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants_DB.DISCLOSE_CREATETIME)));
                        disclose.setTitle(cursor.getString(cursor.getColumnIndex(Constants_DB.DISCLOSE_TITLE)));
                        discloses.add(disclose);
                    }
                }
                event.setDiscloseList(discloses);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void deleteDisclose(final Data_DB_Disclose disclose){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                String conditionStr = Constants_DB.DISCLOSE_DATAID + " = '" + disclose.getId() + "'";
                if(mDataBaseTools.deleteData(Constants_DB.TABLE_DISCLOSE, conditionStr)){
                    ActionEvent event = new ActionEvent(ActionEvent.ACTION_DISCLOSE_DELETE_SUCCESS);
                    EventBus.getDefault().post(event);
                } else {
                    ActionEvent event = new ActionEvent(ActionEvent.ACTION_DISCLOSE_DELETE_FAILED);
                    EventBus.getDefault().post(event);
                }
            }
        });
    }
}
