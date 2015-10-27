package com.bhsc.mobile.discuss.detail;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.utils.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 15-10-10.
 */
public class DiscussDetailPresenter {

    private static DiscussDetailPresenter sDiscussDetailPresenter;

    public static DiscussDetailPresenter getInstance() {
        if (sDiscussDetailPresenter == null) {
            sDiscussDetailPresenter = new DiscussDetailPresenter();
        }
        return sDiscussDetailPresenter;
    }

    public static void destroy() {
        if(sDiscussDetailPresenter != null) {
            sDiscussDetailPresenter.clean();
            sDiscussDetailPresenter = null;
        }
    }

    private ExecutorService mExecutorService;

    private DiscussDetailPresenter() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void getDiscuss() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<Data_DB_Discuss> discusses = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    Data_DB_Discuss discuss = new Data_DB_Discuss();
                    discuss.setCreateTime(Method.getTS());
                    discuss.setContent("打你的年底啊阿森纳的骄傲是南京的哪家啊那经济法");
                    discusses.add(discuss);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DiscussEvent event = new DiscussEvent();
                event.setDiscusses(discusses);
                EventBus.getDefault().post(event);
            }
        });
    }

    private void clean(){
        mExecutorService.shutdownNow();
    }
}
