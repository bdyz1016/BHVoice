package com.bhsc.mobile.disclose;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.pc.ioc.db.sqlite.DbUtils;
import com.android.pc.ioc.db.sqlite.Selector;
import com.android.pc.ioc.db.sqlite.WhereBuilder;
import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.datalcass.Data_DB_Disclose;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.media.FileUtil;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.utils.Constants_DB;
import com.bhsc.mobile.utils.Method;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 10/15/15.
 */
public class DisclosePresenter {

    private ExecutorService mExecutorService;
    private DbUtils mDbUtils;

    private DbUtils.DbUpgradeListener mDbUpgradeListener = new DbUtils.DbUpgradeListener() {
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    };

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
        mDbUtils = DbUtils.create(context, Constants_DB.DB_NAME, Constants_DB.DB_VERSION, mDbUpgradeListener);
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
                mDbUtils.save(disclose);
                EventBus.getDefault().post(new ActionEvent(ActionEvent.ACTION_ADD_DISCLOSE_FINISH));
            }
        });
    }

    public void getAllDisclose(){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                ActionEvent event = new ActionEvent(ActionEvent.ACTION_LOAD_DISCLOSE);
                List<Data_DB_Disclose> list = mDbUtils.findAll(Selector.from(Data_DB_Disclose.class));
                event.setDiscloseList(list);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void deleteDisclose(final Data_DB_Disclose disclose){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mDbUtils.delete(Data_DB_Disclose.class, WhereBuilder.b("DataId", " = " , disclose.getDataId()));
                ActionEvent event = new ActionEvent(ActionEvent.ACTION_DISCLOSE_REFRESH);
                List<Data_DB_Disclose> list = mDbUtils.findAll(Selector.from(Data_DB_Disclose.class));
                event.setDiscloseList(list);
                event.setExtra(disclose);
                EventBus.getDefault().post(event);
            }
        });
    }
}
