package com.bhsc.disclose.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.utils.DateFormat;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.orm.dsl.NotNull;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhanglei on 16/4/5.
 */
public class DiscloseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;

    private final List<Data_DB_Disclose> mDataList = new LinkedList<>();
    public DiscloseRecyclerAdapter(){

    }

    public void addAll(Collection<Data_DB_Disclose> collection){
        mDataList.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case TYPE_HEADER: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_disclose, parent, false);
                viewHolder = new HealderViewHolder(contentView);
                break;
            }
            case TYPE_CONTENT: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disclose, parent, false);
                viewHolder = new ContentViewHolder(contentView);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                HealderViewHolder healderViewHolder = (HealderViewHolder) holder;
                healderViewHolder.mCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case TYPE_CONTENT:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.bind(mDataList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
//        return mDataList.size() + 1;
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if(position == 0){
//            return TYPE_HEADER;
//        }
        return TYPE_CONTENT;
    }

    private static class HealderViewHolder extends RecyclerView.ViewHolder{

        public View mCreate;
        public HealderViewHolder(View itemView) {
            super(itemView);
            mCreate = itemView.findViewById(R.id.create);
        }
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder{

        public TextView Tv_Date, Tv_Time, Tv_Content, Tv_Delete, Tv_Support, Tv_Dicuss, Tv_Share;
        public NineGridImageView mPictures;

        private DateFormat mDateFormat;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Picasso.with(context)
                        .load(s)
                        .placeholder(R.mipmap.icon_no_image)
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }
        };

        public ContentViewHolder(View itemView) {
            super(itemView);
            Tv_Date = (TextView) itemView.findViewById(R.id.date);
            Tv_Time = (TextView) itemView.findViewById(R.id.time);
            Tv_Content = (TextView) itemView.findViewById(R.id.content);
            Tv_Delete = (TextView) itemView.findViewById(R.id.delete);
            Tv_Support = (TextView) itemView.findViewById(R.id.support);
            Tv_Dicuss = (TextView) itemView.findViewById(R.id.discuss);
            Tv_Share = (TextView) itemView.findViewById(R.id.share);
            mPictures = (NineGridImageView) itemView.findViewById(R.id.pictures);
            mPictures.setAdapter(mAdapter);
            mDateFormat = new DateFormat();
        }

        public void bind(Data_DB_Disclose disclose) {
            mPictures.setImagesData(Arrays.asList(disclose.getImgs().split(",")));
            Tv_Content.setText(disclose.getContent());
            String[] date = mDateFormat.format(disclose.getCreateTime());
            Tv_Date.setText(date[0]);
            Tv_Time.setText(date[1]);
        }
    }
}
