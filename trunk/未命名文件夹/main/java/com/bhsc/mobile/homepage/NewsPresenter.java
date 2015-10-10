package com.bhsc.mobile.homepage;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.utils.L;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 15-10-8.
 */
public class NewsPresenter {
    private final String TAG = NewsPresenter.class.getSimpleName();

    private static NewsPresenter sNewsPresenter;

    public static NewsPresenter getInstance(){
        if(sNewsPresenter == null){
            sNewsPresenter = new NewsPresenter();
        }
        return sNewsPresenter;
    }

    private ExecutorService mExecutorService;

    private NewsPresenter(){
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void getNews(){
        L.i(TAG, "getNews");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<Data_DB_News> newsList = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    Data_DB_News news = new Data_DB_News();
                    if((i + 1) % 5 == 0){
                        news.setType(Data_DB_News.TYPE_ADVERTISE);
                    } else {
                        news.setType(Data_DB_News.TYPE_NEWS);
                    }
                    news.setTitle("手机流量不清零10月实施 未击中消费者痛点");
                    newsList.add(news);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(newsList);
            }
        });
    }
}
