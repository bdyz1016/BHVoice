package com.bhsc.mobile.homepage.newsdetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnItemClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.homepage.NewsPresenter;
import com.bhsc.mobile.homepage.adapter.NewsAdapter;
import com.bhsc.mobile.discuss.detail.DiscussDetailActivity;
import com.bhsc.mobile.homepage.newsdetail.adapter.HotDiscussAdapter;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;
import com.bhsc.mobile.utils.SyncArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynn on 15-9-19.
 */
@InjectLayer(R.layout.activity_newsdetail)
public class DetailActivity extends Activity {
    private final String TAG = DetailActivity.class.getName();

    @InjectAll
    private Views mViews;

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_newsdetail_title_back;
        @InjectBinder(method = "opposition", listeners = {OnClick.class})
        View activity_newsdetail_opposition;
        @InjectBinder(method = "collect", listeners = {OnClick.class})
        View activity_newsdetail_collect;
        @InjectBinder(method = "support", listeners = {OnClick.class})
        View activity_newsdetail_support;
        @InjectBinder(method = "discuss", listeners = {OnClick.class})
        View activity_newsdetail_discuss;
        ImageView activity_newsdetail_support_icon, activity_newsdetail_opposition_icon, activity_newsdetail_collect_icon;
        @InjectBinder(method = "onNewsItemClick", listeners = {OnItemClick.class})
        MyListView activity_newsdetail_news_list;
        @InjectBinder(method = "onDiscussItemClick", listeners = {OnItemClick.class})
        MyListView activity_newsdetail_discuss_list;
        View activity_newsdetail_related_news, activity_newsdetail_hot_discuss, activity_newsdetail_news_list_divider;
    }

    private PopupWindow mPopupWindow;
    private NewsAdapter mNewsAdapter;
    private HotDiscussAdapter mHotDiscussAdapter;
    private final SyncArrayList<Data_DB_News> mNewsList = new SyncArrayList<>(new ArrayList<Data_DB_News>());
    private final List<Data_DB_Discuss> mDiscusses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @InjectInit
    private void initView(){
        mNewsAdapter = new NewsAdapter(this, mNewsList);
        mHotDiscussAdapter = new HotDiscussAdapter(this, mDiscusses);
        mViews.activity_newsdetail_news_list.setAdapter(mNewsAdapter);
        mViews.activity_newsdetail_news_list.setFocusable(false);
        mViews.activity_newsdetail_discuss_list.setAdapter(mHotDiscussAdapter);
        mViews.activity_newsdetail_discuss_list.setFocusable(false);
        NewsPresenter.getInstance(this).getRelatedNews();
        NewsPresenter.getInstance(this).getHotDiscuss();
    }

    private void goBack(){
        finish();
    }

    private void support(){
        NewsPresenter.getInstance(this).newsSupport();
    }

    private void opposition(){
        NewsPresenter.getInstance(this).newsOpp();
    }

    private void collect(){
        NewsPresenter.getInstance(this).newsCollect();
    }

    private void discuss(){
        showDialag();
    }

    private void onNewsItemClick(){
        Intent intent = new Intent();
        intent.setClass(this, DetailActivity.class);
        startActivity(intent);
        finish();
    }

    private void onDiscussItemClick(){
        Intent intent = new Intent();
        intent.setClass(this, DiscussDetailActivity.class);
        startActivity(intent);
    }

    private void onEventMainThread(SupportEvent event){
        if(event.getState() == SupportEvent.STATE_NEGATIVE){
            mViews.activity_newsdetail_support_icon.setBackgroundResource(R.mipmap.btn_support_big_normal);
        } else {
            mViews.activity_newsdetail_support_icon.setBackgroundResource(R.mipmap.btn_support_big_press);
        }
    }

    private void onEventMainThread(CollectEvent event){
        if(event.getState() == SupportEvent.STATE_NEGATIVE){
            mViews.activity_newsdetail_collect_icon.setBackgroundResource(R.mipmap.btn_collect_normal);
        } else {
            mViews.activity_newsdetail_collect_icon.setBackgroundResource(R.mipmap.btn_collect_press);
        }
    }

    private void onEventMainThread(OppEvent event){
        if(event.getState() == SupportEvent.STATE_NEGATIVE){
            mViews.activity_newsdetail_opposition_icon.setBackgroundResource(R.mipmap.btn_opposition_normal);
        } else {
            mViews.activity_newsdetail_opposition_icon.setBackgroundResource(R.mipmap.btn_opposition_press);
        }
    }

    private void onEventMainThread(RelatedNewsEvent event){
        List<Data_DB_News> newsList = event.getNewsList();
        L.i(TAG, "初始化新闻列表:" + newsList.size());
        if(newsList.size() > 0){
            mViews.activity_newsdetail_related_news.setVisibility(View.VISIBLE);
            mViews.activity_newsdetail_news_list_divider.setVisibility(View.VISIBLE);
        }
        mNewsList.addAll(newsList);
        mNewsAdapter.notifyDataSetChanged();
    }

    private void onEventMainThread(HotDiscussEvent event){
        List<Data_DB_Discuss> discusses = event.getDiscusses();
        L.i(TAG, "初始化评论列表:" + discusses.size());
        if(discusses.size() > 0){
            mViews.activity_newsdetail_hot_discuss.setVisibility(View.VISIBLE);
        }
        mDiscusses.clear();
        mDiscusses.addAll(discusses);
        mHotDiscussAdapter.notifyDataSetChanged();
    }

    private void showDialag() {
        backgroundAlpha(0.4f);

        View view = LayoutInflater.from(this).inflate(R.layout.pop_input_discuss, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setAnimationStyle(R.style.pop_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        final Button status = (Button) view.findViewById(R.id.pop_input_discuss_status);
        final EditText text = (EditText) view.findViewById(R.id.pop_input_discuss_edit);
        Button publish = (Button) view.findViewById(R.id.pop_input_discuss_publish);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean toUserStatus = (Boolean)status.getTag();
                if(toUserStatus == null || !toUserStatus){
                    status.setTag(true);
                    status.setBackgroundResource(R.mipmap.btn_status_press);
                } else {
                    status.setTag(false);
                    status.setBackgroundResource(R.mipmap.btn_status_normal);
                }
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discussContent = text.getText().toString();
                if(TextUtils.isEmpty(discussContent)){
                    return;
                }
                Data_DB_Discuss discuss = new Data_DB_Discuss();
                discuss.setContent(discussContent);
                Boolean toUserStatus = (Boolean)status.getTag();
                if(toUserStatus == null || !toUserStatus){
                    discuss.setToUserStatus(false);
                } else {
                    discuss.setToUserStatus(true);
                }
                discuss.setCreateTime(Method.getTS());
                NewsPresenter.getInstance(DetailActivity.this).discussPushlish(discuss);
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.showAtLocation(findViewById(R.id.activity_newsdetail_container),
                Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
