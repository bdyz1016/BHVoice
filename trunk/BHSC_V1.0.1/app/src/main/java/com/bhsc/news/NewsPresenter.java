//package com.bhsc.news;
//
//import android.content.Context;
//
//import com.bhsc.mobile.R;
//import com.bhsc.news.model.Data_DB_NewsType;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by lynn on 15-10-8.
// */
//public class NewsPresenter {
//    private final String TAG = NewsPresenter.class.getSimpleName();
//
//    public static final int LOAD_NEWS_PAGE_SIZE = 20;
//
//    private static NewsPresenter sNewsPresenter;
//
//    public static NewsPresenter getInstance(Context context) {
//        if (sNewsPresenter == null) {
//            synchronized (NewsPresenter.class) {
//                if (sNewsPresenter == null) {
//                    sNewsPresenter = new NewsPresenter(context);
//                }
//            }
//        }
//        return sNewsPresenter;
//    }
//
//    private ExecutorService mExecutorService;
//    private Gson mGson;
//
//    private DownloadControler mDownloadControler;
//    private NewsManager mNewsManager;
//
//    private NewsPresenter(Context context) {
//        mExecutorService = Executors.newCachedThreadPool();
//        mGson = new Gson();
//        mDownloadControler = new DownloadControler(context);
//        mNewsManager = new NewsManager(context);
//    }
//
//    /**
//     * 获取新闻类型
//     *
//     * @param context
//     * @return
//     */
//    public ArrayList<Data_DB_NewsType> getNewsTypes(Context context) {
//        return mDownloadControler.getNewsTypes();
//    }
//
//    /**
//     * 获取新闻列表
//     *
//     * @param newsType 新闻类型
//     */
//    public void getNews(final int newsType, final int page) {
//        L.i(TAG, "getNews");
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                if (newsType == 0) {
//                    params.put("isHot", "1");
//                    params.put("page", page + "");
//                } else {
//                    params.put("isHot", "0");
//                    params.put("type", newsType + "");
//                    params.put("page", page + "");
//                }
//                try {
//                    httpPost httpPost = new httpPost();
//                    String back = httpPost.requireClass(BHApplication.Address + "/news/getNews", params, "UTF-8");
//                    L.i(TAG, back);
//                    NewsResponse newsResponse = mGson.fromJson(back, new TypeToken<NewsResponse>() {
//                    }.getType());
//                    if (newsResponse.getCode() == Response.SUCESS_CODE) {
//                        List<Data_DB_News> newsList = newsResponse.getList();
//                        mNewsManager.saveNews(newsList);
//                        NewsEvent event = new NewsEvent();
//                        event.setAction(NewsEvent.ACTION_REFRESH_COMPLETE);
//                        event.setNewsType(newsType);
//                        event.setNewsList(newsList);
//                        EventBus.getDefault().post(event);
//                    } else { //请求失败
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 相关阅读
//     */
//    public void getRelatedNews() {
//        L.i(TAG, "getRelatedNews");
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                List<Data_DB_News> newsList = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    Data_DB_News news = new Data_DB_News();
//                    news.setIsAdv(Data_DB_News.TYPE_NEWS);
//                    news.setTitle("手机流量不清零10月实施 未击中消费者痛点");
//                    newsList.add(news);
//                }
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                RelatedNewsEvent event = new RelatedNewsEvent();
//                event.setNewsList(newsList);
//                EventBus.getDefault().post(event);
//            }
//        });
//    }
//
//    public Data_DB_News getSpecificNews(final String newsId){
//        return mNewsManager.getNews(newsId);
//    }
//
//    /**
//     * 用户对新闻点了赞
//     */
//    public void newsSupport(final Data_DB_News news, final String userId) {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                SupportEvent event = new SupportEvent();
//                httpPost httpPost = new httpPost();
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                params.put("type", news.getType() + "");
//                params.put("userId", userId);
//                params.put("fatherId", news.getId());
//                try {
//                    String response = httpPost.requireClass(BHApplication.Address + "/praise/addPraise", params, "UTF-8");
//                    PraiseResponse praiseResponse = mGson.fromJson(response, PraiseResponse.class);
//                    if (praiseResponse != null && praiseResponse.getCode() == Response.SUCESS_CODE) {
//                        event.setState(SupportEvent.STATE_POSITIVE);
//                    } else {
//                        event.setState(SupportEvent.STATE_NEGATIVE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                EventBus.getDefault().post(event);
//            }
//        });
//    }
//
//    boolean collect = false;
//
//    /**
//     * 用户收藏了新闻
//     */
//    public void newsCollect() {
//        CollectEvent event = new CollectEvent();
//        if (!collect) {
//            collect = true;
//            event.setState(SupportEvent.STATE_POSITIVE);
//        } else {
//            collect = false;
//            event.setState(SupportEvent.STATE_NEGATIVE);
//        }
//        EventBus.getDefault().post(event);
//    }
//
//    boolean opp = false;
//
//    /**
//     * 用户踩了新闻
//     */
//    public void newsOpp() {
//        OppEvent event = new OppEvent();
//        if (!opp) {
//            opp = true;
//            event.setState(SupportEvent.STATE_POSITIVE);
//        } else {
//            opp = false;
//            event.setState(SupportEvent.STATE_NEGATIVE);
//        }
//        EventBus.getDefault().post(event);
//    }
//
//    /**
//     * 发表评论
//     */
//    public void discussPublish(final Data_DB_Discuss discuss) {
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                httpPost httpPost = new httpPost();
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                params.put("type", discuss.getType() + "");
//                params.put("userId", discuss.getUserId());
//                params.put("fatherId", discuss.getFatherId());
//                params.put("content", discuss.getContent());
//                try {
//                    String response = httpPost.requireClass(BHApplication.Address + "/comment/addComment", params, "UTF-8");
//                    DiscussResponse discussResponse = mGson.fromJson(response, DiscussResponse.class);
//                    DiscussEvent event = new DiscussEvent();
//                    if (discussResponse != null && discussResponse.getCode() == Response.SUCESS_CODE) {
//                        event.setAction(DiscussEvent.ACTION_PUBLISH_SUCCESS);
//                    } else {
//                        event.setAction(DiscussEvent.ACTION_PUBLISH_FAILED);
//                    }
//                    EventBus.getDefault().post(event);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    public void getAllDiscuss(final Data_DB_News news){
//        mExecutorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                httpPost httpPost = new httpPost();
//                LinkedHashMap<String, String> params = new LinkedHashMap<>();
//                params.put("type", news.getType() + "");
//                params.put("fatherId", news.getId());
//                try {
//                    String response = httpPost.requireClass(BHApplication.Address + "/comment/findComments", params, "UTF-8");
//                    DiscussResponse discussResponse = mGson.fromJson(response, DiscussResponse.class);
//                    DiscussEvent event = new DiscussEvent();
//                    if (discussResponse != null && discussResponse.getCode() == Response.SUCESS_CODE) {
//                        event.setAction(DiscussEvent.ACTION_GET_ALL_DISCUSS);
//                        event.setExtra(discussResponse.getList());
//                        EventBus.getDefault().post(event);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    /**
//     * 热门评论
//     */
////    public void getHotDiscuss() {
////        L.i(TAG, "getRelatedNews");
////        mExecutorService.submit(new Runnable() {
////            @Override
////            public void run() {
////                List<Data_DB_Discuss> discusses = new ArrayList<>();
////                for (int i = 0; i < 5; i++) {
////                    Data_DB_Discuss discuss = new Data_DB_Discuss();
////                    discuss.setCreateTime(Method.getTS());
////                    discuss.setContent("打你的年底啊阿森纳的骄傲是南京的哪家啊那经济法");
////                    discusses.add(discuss);
////                }
////                try {
////                    Thread.sleep(2000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                HotDiscussEvent event = new HotDiscussEvent();
////                event.setDiscusses(discusses);
////                EventBus.getDefault().post(event);
////            }
////        });
////    }
//
//    private class DownloadControler {
//
//        private Context mContext;
//
//        public DownloadControler(Context context) {
//            this.mContext = context;
//
//            ArrayList<Data_DB_NewsType> typeList = getNewsTypes();
//        }
//
//        public ArrayList<Data_DB_NewsType> getNewsTypes() {
//            ArrayList<Data_DB_NewsType> types = new ArrayList<>();
//            String[] defaultTypes = mContext.getResources().getStringArray(R.array.news_category);
//            int length = defaultTypes.length;
//            for (int i = 0; i < length; i++) {
//                Data_DB_NewsType type = new Data_DB_NewsType();
//                type.setTypeId(i);
//                type.setTypeName(defaultTypes[i]);
//                types.add(type);
//            }
//            return types;
//        }
//    }
//}
