package com.bhsc.mobile.userpages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_Discuss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lynn on 15-10-8.
 */
public class DiscussAdapter extends BaseAdapter {

    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final long HOUR = 60 * 60 * 1000;
    private static final long MINUTE = 60 * 1000;

    private String mHour;
    private String mMinute;
    private String mJust;

    private SimpleDateFormat mSimpleDateFormat;

    private LayoutInflater mInflater;
    private List<Data_DB_Discuss> mDiscusses;

    public DiscussAdapter(Context context, List<Data_DB_Discuss> discusses){
        mInflater = LayoutInflater.from(context);
        mDiscusses = discusses;

        mHour = "小时前";
        mMinute = "分钟前";
        mJust = "刚刚";
        mSimpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
    }

    @Override
    public int getCount() {
        return mDiscusses.size();
    }

    @Override
    public Object getItem(int position) {
        return mDiscusses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_mydiscuss, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Data_DB_Discuss discuss = mDiscusses.get(position);
        holder.Tv_Content.setText(discuss.getContent());
        holder.Tv_Time.setText(getTime(discuss.getCreateTime()));
        return convertView;
    }

    private String getTime(long ts){
        String time;
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - ts * 1000;
        if(interval < DAY){
            if(interval > HOUR){
                time = Math.floor(interval / HOUR) + mHour;
            } else if(interval > MINUTE){
                time = Math.floor(interval / MINUTE) + mMinute;
            } else {
                time = mJust;
            }
        } else {
            time = mSimpleDateFormat.format(new Date(ts * 1000));
        }
        return time;
    }

    private class ViewHolder{
        private TextView Tv_NickName, Tv_Time, Tv_Content;
        public ViewHolder(View view){
            Tv_NickName = (TextView) view.findViewById(R.id.item_mydiscuss_nickname);
            Tv_Time = (TextView) view.findViewById(R.id.item_mydiscuss_time);
            Tv_Content = (TextView) view.findViewById(R.id.item_mydiscuss_content);
        }
    }
}
