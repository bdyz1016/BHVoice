package com.bhsc.mobile.disclose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.datalcass.Data_DB_Disclose;
import com.bhsc.mobile.utils.SyncArrayList;

/**
 * Created by lynn on 15-9-30.
 */
public class DiscloseAdapter extends BaseAdapter {

    private SyncArrayList<Data_DB_Disclose> mDataList;
    private Context mContext;
    private LayoutInflater mInflater;

    private DiscloseAdapter(Context context, SyncArrayList<Data_DB_Disclose> dataList){
        this.mContext = context;
        this.mDataList = dataList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_disclose, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder{
        private TextView Tv_Date, Tv_Time, Tv_Content;
        private View Delete, Support, Share;
        public ViewHolder(View view){
            Tv_Date = (TextView)view.findViewById(R.id.item_disclose_date);
            Tv_Time = (TextView)view.findViewById(R.id.item_disclose_time);
            Tv_Content = (TextView)view.findViewById(R.id.item_disclose_content);
            Delete = (TextView)view.findViewById(R.id.item_disclose_content);
        }
    }
}
