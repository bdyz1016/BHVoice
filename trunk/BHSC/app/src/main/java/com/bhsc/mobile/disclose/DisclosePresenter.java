package com.bhsc.mobile.disclose;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.database.Constants_DB;
import com.bhsc.mobile.database.DataBaseTools;
import com.bhsc.mobile.dataclass.Data_DB_Disclose;
import com.bhsc.mobile.dataclass.Data_DB_Picture;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.media.MediaManager;
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

    private MediaManager mMediaManager;

    private DiscloseManager mDiscloseManager;

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
        mMediaManager = new MediaManager(context);
        mDiscloseManager = new DiscloseManager(context);
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

    public void publish(final Data_DB_Disclose disclose, final List<String> picturePaths){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                disclose.setId(Method.createDataId());
                disclose.setImagePaths(picturePaths);
                mDataBaseTools.addData(Constants_DB.TABLE_DISCLOSE, disclose);//保存爆料
                Data_DB_Picture picture = new Data_DB_Picture();
                picture.setId(disclose.getId());
                for(String path:picturePaths){
                    picture.setPath(path);
                    mMediaManager.savePicturePath(picture);//保存图片
                }
                EventBus.getDefault().post(new ActionEvent(ActionEvent.ACTION_ADD_DISCLOSE_FINISH));
            }
        });
    }

    public void getAllDisclose(){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                ArrayList<Data_DB_Disclose> discloses = mDiscloseManager.getAllDisclose();
                ActionEvent event = new ActionEvent(ActionEvent.ACTION_LOAD_DISCLOSE);
                event.setDiscloseList(discloses);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void support(final String dataId){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Data_DB_Disclose disclose = mDiscloseManager.getDisclose(dataId);
                disclose.setPraiseCount(disclose.getPraiseCount() + 1);
                mDiscloseManager.updataDisclose(disclose);
                ActionEvent event = new ActionEvent(ActionEvent.ACTION_PRAISE_SUCCESS);
                EventBus.getDefault().post(event);
            }
        });
    }

    public void deleteDisclose(final Data_DB_Disclose disclose){
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if(mDiscloseManager.deleteDisclose(disclose)){//删除爆料
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
