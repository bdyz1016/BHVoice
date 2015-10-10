package com.bhsc.mobile.userpages;

import android.app.Activity;
import android.content.Intent;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 15-10-7.
 */
public class UserPresenter {
    private final String TAG = UserPresenter.class.getSimpleName();

    private static UserPresenter sUserPresenter;

    public static UserPresenter getInstance(){
        if(sUserPresenter == null){
            sUserPresenter = new UserPresenter();
        }
        return sUserPresenter;
    }

    public static void destroy(){
        sUserPresenter = null;
    }

    private ExecutorService mExecutorService;

    private UserPresenter(){
        mExecutorService = Executors.newCachedThreadPool();
//        mExecutorService = Executors.new
    }

    public void initUserData(){
        L.i(TAG, "initUserData");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                L.i(TAG, "create discusses");
                ArrayList<Data_DB_Discuss> discusses = new ArrayList<>();
                for(int i = 0;i<10;i++){
                    Data_DB_Discuss data = new Data_DB_Discuss();
                    data.setContent("屠呦呦宁波旧居要卖1.5亿 吐槽：现在满世界都是我");
                    data.setCreateTime(Method.getTS());
                    discusses.add(data);
                }
                DiscussEvent event = new DiscussEvent();
                event.setDiscusses(discusses);
                EventBus.getDefault().post(event);
            }
        });
    }

    private void pickPhotoFromGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
        activity.startActivityForResult(intent, requestCode);
    }
}
