package com.bhsc.mobile.comment;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.comment.model.Data_DB_Comment;
import com.bhsc.mobile.utils.DateFormat;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhanglei on 16/4/27.
 */
public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private DateFormat mDateFormat;
    private final List<Data_DB_Comment> mCommentList = new LinkedList<>();

    public CommentAdapter(Context context) {
        mContext = context;
        mDateFormat = new DateFormat();
    }

    public void add(Data_DB_Comment comment){
        mCommentList.add(comment);
        notifyDataSetChanged();
    }

    public void addAll(List<Data_DB_Comment> list){
        mCommentList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        mCommentList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_discuss, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.bind(mCommentList.get(position));
        return convertView;
    }

    private class ViewHolder {
        private SimpleDraweeView mPhoto;
        private TextView Tv_Name, Tv_AddTime, Tv_Content;
        public ViewHolder(View itemView) {
            mPhoto = (SimpleDraweeView) itemView.findViewById(R.id.photo);
            Tv_Name = (TextView) itemView.findViewById(R.id.name);
            Tv_AddTime = (TextView) itemView.findViewById(R.id.time);
            Tv_Content = (TextView) itemView.findViewById(R.id.content);
        }

        public void bind(Data_DB_Comment comment){
            String url = comment.getHeadurl();
            if(!TextUtils.isEmpty(url)) {
                mPhoto.setImageURI(Uri.parse(url));
            }
            Tv_Name.setText(comment.getUsername());
            Tv_Content.setText(comment.getContent());
            String[] date = mDateFormat.format(comment.getTime());
            Tv_AddTime.setText(date[0] + " " + date[1]);
        }
    }
}
