package com.bhsc.mobile.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_News;
import com.bhsc.mobile.homepage.NewsFragment;
import com.bhsc.mobile.utils.SyncArrayList;

/**
 * Created by lynn on 15-9-17.
 */
public class NewsAdapter extends BaseAdapter {

    private final int ITEM_TYPE_NEWS = 0;
    private final int ITEM_TYPE_ADVERTISE = 1;

    private final int ITEM_TYPE_COUNT = 2;

    private SyncArrayList<Data_DB_News> mNewsList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context, SyncArrayList<Data_DB_News> news){
        this.mInflater = LayoutInflater.from(context);
        this.mNewsList = news;
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
        if(convertView == null){
            convertView = createView(news, parent);
            holder = new ViewHolder();
            if(news.getIsAdv() == Data_DB_News.TYPE_NEWS) {
                holder.Tv_NewsTitle = (TextView) convertView.findViewById(R.id.item_news_title);
                holder.Iv_Image = (ImageView) convertView.findViewById(R.id.item_news_image);
            } else if(news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE){
                holder.Iv_Image = (ImageView) convertView.findViewById(R.id.item_advertise_image);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(news.getIsAdv() == Data_DB_News.TYPE_NEWS) {
            holder.Tv_NewsTitle.setText(news.getTitle());
            getDrawable("", holder.Iv_Image);
        } else if(news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE){
            getDrawable("", holder.Iv_Image);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Data_DB_News news = mNewsList.get(position);
        if(news.getIsAdv() == Data_DB_News.TYPE_NEWS){
            return ITEM_TYPE_NEWS;
        } else if(news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE){
            return ITEM_TYPE_ADVERTISE;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    private View createView(Data_DB_News news, ViewGroup parent){
        View view = null;
        if(news.getIsAdv() == Data_DB_News.TYPE_NEWS){
            view = mInflater.inflate(R.layout.item_news, parent, false);
        } else if(news.getIsAdv() == Data_DB_News.TYPE_ADVERTISE){
            view = mInflater.inflate(R.layout.item_advertise, parent, false);
        }
        return view;
    }

    private void getDrawable(String url, ImageView imageView) {
        NewsFragment.mImageFetcher.loadImage(url, imageView);
    }

    private class ViewHolder{
        private TextView Tv_NewsTitle;
        private ImageView Iv_Image;
    }
}
