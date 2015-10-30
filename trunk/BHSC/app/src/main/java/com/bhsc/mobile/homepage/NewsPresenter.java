package com.bhsc.mobile.homepage;

import android.content.Context;

import com.android.pc.ioc.event.EventBus;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.datalcass.Data_DB_NewsType;
import com.bhsc.mobile.homepage.event.NewsEvent;
import com.bhsc.mobile.homepage.newsdetail.CollectEvent;
import com.bhsc.mobile.homepage.newsdetail.HotDiscussEvent;
import com.bhsc.mobile.homepage.newsdetail.OppEvent;
import com.bhsc.mobile.homepage.newsdetail.RelatedNewsEvent;
import com.bhsc.mobile.homepage.newsdetail.SupportEvent;
import com.bhsc.mobile.main.BHApplication;
import com.bhsc.mobile.net.NewsResponse;
import com.bhsc.mobile.net.httpPost;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lynn on 15-10-8.
 */
public class NewsPresenter {
    private final String TAG = NewsPresenter.class.getSimpleName();

    private static NewsPresenter sNewsPresenter;

    public static NewsPresenter getInstance(Context context) {
        if(sNewsPresenter == null){
            synchronized (NewsPresenter.class){
                if(sNewsPresenter == null){
                    sNewsPresenter  =new NewsPresenter(context);
                }
            }
        }
        return sNewsPresenter;
    }

    private ExecutorService mExecutorService;
    private Gson mGson;

    private DownloadControler mDownloadControler;

    private NewsPresenter(Context context) {
        mExecutorService = Executors.newCachedThreadPool();
        mGson = new Gson();
        mDownloadControler = new DownloadControler(context);
    }

    /**
     * 获取新闻类型
     *
     * @param context
     * @return
     */
    public ArrayList<Data_DB_NewsType> getNewsTypes(Context context) {
        return mDownloadControler.getNewsTypes();
    }

    /**
     * 获取新闻列表
     *
     * @param newsType 新闻类型
     */
    public void getNews(final int newsType) {
        L.i(TAG, "getNews");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                int page = mDownloadControler.getPageNumber(newsType);
                if(newsType == 0){
                    params.put("isHot", "1");
                    params.put("page", page + "");
                } else {
                    params.put("isHot", "1");
                    params.put("type", "1");
                    params.put("page", page + "");
                }
                try {
                    httpPost httpPost = new httpPost();
                    String back = httpPost.requireClass(BHApplication.Address + "/news/getNews", params, "UTF-8");
                    NewsResponse newsResponse = mGson.fromJson(back, NewsResponse.class);
                    if (newsResponse.getCode() == 200) {
                        if(newsResponse.getList().size() > 0) {
                            NewsEvent event = new NewsEvent();
                            event.setAction(NewsEvent.ACTION_REFRESH_COMPLETE);
                            event.setNewsType(newsType);
                            event.setNewsList(newsResponse.getList());
                            EventBus.getDefault().post(event);
                        } else {

                        }
                    } else { //请求失败

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 刷新新闻
     * @param newsType
     */
    public void refreshNews(final int newsType){
        L.i(TAG, "refreshNews");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                if(newsType == 0){
                    params.put("isHot", "1");
                    params.put("page", "2");
                } else {
                    params.put("isHot", "1");
                    params.put("type", "1");
                    params.put("page", "1");
                }
                httpPost httpPost = new httpPost();
                try {
                    String back = httpPost.requireClass(BHApplication.Address + "/news/getNews", params, "UTF-8");
                    NewsResponse newsResponse = mGson.fromJson(back, NewsResponse.class);
                    if (newsResponse.getCode() == 200) {
                        if(newsResponse.getList().size() > 0) {
                            NewsEvent event = new NewsEvent();
                            event.setAction(NewsEvent.ACTION_REFRESH_COMPLETE);
                            event.setNewsType(newsType);
                            event.setNewsList(newsResponse.getList());
                            EventBus.getDefault().post(event);
                        } else {

                        }
                    } else { //请求失败

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 相关阅读
     */
    public void getRelatedNews() {
        L.i(TAG, "getRelatedNews");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                List<Data_DB_News> newsList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    Data_DB_News news = new Data_DB_News();
                    news.setIsAdv(Data_DB_News.TYPE_NEWS);
                    news.setTitle("手机流量不清零10月实施 未击中消费者痛点");
                    newsList.add(news);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RelatedNewsEvent event = new RelatedNewsEvent();
                event.setNewsList(newsList);
                EventBus.getDefault().post(event);
            }
        });
    }

    /**
     * 热门评论
     */
    public void getHotDiscuss() {
        L.i(TAG, "getRelatedNews");
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
                HotDiscussEvent event = new HotDiscussEvent();
                event.setDiscusses(discusses);
                EventBus.getDefault().post(event);
            }
        });
    }

    boolean support = false;

    /**
     * 用户对新闻点了赞
     */
    public void newsSupport() {
        SupportEvent event = new SupportEvent();
        if (!support) {
            support = true;
            event.setState(SupportEvent.STATE_POSITIVE);
        } else {
            support = false;
            event.setState(SupportEvent.STATE_NEGATIVE);
        }
        EventBus.getDefault().post(event);
    }

    boolean collect = false;

    /**
     * 用户收藏了新闻
     */
    public void newsCollect() {
        CollectEvent event = new CollectEvent();
        if (!collect) {
            collect = true;
            event.setState(SupportEvent.STATE_POSITIVE);
        } else {
            collect = false;
            event.setState(SupportEvent.STATE_NEGATIVE);
        }
        EventBus.getDefault().post(event);
    }

    boolean opp = false;

    /**
     * 用户踩了新闻
     */
    public void newsOpp() {
        OppEvent event = new OppEvent();
        if (!opp) {
            opp = true;
            event.setState(SupportEvent.STATE_POSITIVE);
        } else {
            opp = false;
            event.setState(SupportEvent.STATE_NEGATIVE);
        }
        EventBus.getDefault().post(event);
    }

    /**
     * 发表评论
     */
    public void discussPushlish(Data_DB_Discuss discuss) {
    }

    private class DownloadControler{

        private Context mContext;
        private Map<Integer, Integer> mPageMap;

        public DownloadControler(Context context){
            this.mContext = context;
            this.mPageMap = new HashMap<>();

            ArrayList<Data_DB_NewsType> typeList = getNewsTypes();
            for(Data_DB_NewsType news:typeList){
                mPageMap.put(news.getTypeId(), 1);
            }
        }

        public int getPageNumber(int type){
            return mPageMap.get(type);
        }

        public void putPageNumber(int type, int page){
            mPageMap.put(type, page);
        }

        public ArrayList<Data_DB_NewsType> getNewsTypes(){
            ArrayList<Data_DB_NewsType> types = new ArrayList<>();
            String[] defaultTypes = mContext.getResources().getStringArray(R.array.news_category);
            int length = defaultTypes.length;
            for (int i = 0; i < length; i++) {
                Data_DB_NewsType type = new Data_DB_NewsType();
                type.setTypeId(i);
                type.setTypeName(defaultTypes[i]);
                types.add(type);
            }
            return types;
        }
    }
}
