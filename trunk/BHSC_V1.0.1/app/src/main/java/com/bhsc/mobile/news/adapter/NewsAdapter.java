package com.bhsc.mobile.news.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.news.model.Data_DB_News;
import com.bhsc.mobile.utils.SyncArrayList;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsAdapter extends BaseAdapter {

    private final int ITEM_TYPE_NEWS = 0;
    private final int ITEM_TYPE_ADVERTISE = 1;

    private final int ITEM_TYPE_COUNT = 2;

    private SyncArrayList<Data_DB_News> mNewsList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mNewsList = new SyncArrayList<>(new LinkedList<Data_DB_News>());
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data_DB_News news = mNewsList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_news, parent, false);
            holder = new ViewHolder();
            holder.Tv_NewsTitle = (TextView) convertView.findViewById(R.id.item_news_title);
            holder.Image = (SimpleDraweeView) convertView.findViewById(R.id.item_news_image);
            holder.Tv_Discuss = (TextView) convertView.findViewById(R.id.item_news_discuss);
            holder.Tv_Collect = (TextView) convertView.findViewById(R.id.item_news_collect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Tv_NewsTitle.setText(news.getTitle());
        Uri uri = Uri.parse(news.getTitleImg());
        holder.Image.setImageURI(uri);
        if (news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE) {

        }
        holder.Tv_Discuss.setText(news.getCommentCount() + "");
        holder.Tv_Collect.setText(news.getPraiseCount() + "");
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Data_DB_News news = mNewsList.get(position);
        if (news.getIsAdv() == Data_DB_News.TYPE_NEWS) {
            return ITEM_TYPE_NEWS;
        } else if (news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE) {
            return ITEM_TYPE_ADVERTISE;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

//    private View createView(Data_DB_News news, ViewGroup parent){
//        View view = null;
//        if(news.getIsAdv() == Data_DB_News.TYPE_NEWS){
//            view = mInflater.inflate(R.layout.item_news, parent, false);
//        } else if(news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE){
//            view = mInflater.inflate(R.layout.item_advertise, parent, false);
//        }
//        return view;
//    }

    private class ViewHolder {
        private TextView Tv_NewsTitle, Tv_Discuss, Tv_Collect;
        private SimpleDraweeView Image;
    }

    public synchronized void addAll(List<Data_DB_News> news) {
        mNewsList.addAll(news);
        notifyDataSetChanged();
    }

    public synchronized void clear() {
        mNewsList.clear();
        notifyDataSetChanged();
    }
}
