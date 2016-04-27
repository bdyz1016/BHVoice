package com.bhsc.mobile.disclose.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.DateFormat;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhanglei on 16/4/5.
 */
public class DiscloseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int STATE_LOADING = 0;
    public static final int STATE_LOAD_COMPLETE = 1;
    public static final int STATE_NO_MORE = 2;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_CONTENT = 2;

    private final List<Data_DB_Disclose> mDataList = new LinkedList<>();

    private boolean mHasFooter = false;
    private boolean mHasHeader = false;

    private int mCurrentState = STATE_LOADING;

    public DiscloseRecyclerAdapter() {
    }

    public void addAll(Collection<Data_DB_Disclose> collection) {
        mDataList.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void hasFooter() {
        mHasFooter = true;
    }

    public void hasHeader() {
        mHasHeader = true;
    }

    public void setState(int state){
        if(state == mCurrentState){
            return;
        }
        if(state != STATE_LOAD_COMPLETE && state != STATE_LOADING && state != STATE_NO_MORE){
            return;
        }
        mCurrentState = state;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HEADER: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_disclose, parent, false);
                viewHolder = new HeaderViewHolder(contentView);
                break;
            }
            case TYPE_CONTENT: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disclose, parent, false);
                viewHolder = new ContentViewHolder(contentView);
                break;
            }
            case TYPE_FOOTER: {
//                View contentView = new LoadingMoreFooter(parent.getContext());
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_news_list, parent, false);
                viewHolder = new FooterViewHolder(contentView);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                HeaderViewHolder healderViewHolder = (HeaderViewHolder) holder;
                healderViewHolder.mCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                switch (mCurrentState){
                    case STATE_LOADING:
                        footerViewHolder.loading();
                        break;
                    case STATE_LOAD_COMPLETE:
                        footerViewHolder.complete();
                        break;
                    case STATE_NO_MORE:
                        footerViewHolder.noMore();
                        break;
                    default:
                        break;
                }
                break;
            case TYPE_CONTENT:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                if ((mHasHeader && position >= mDataList.size())
                        || (!mHasHeader && position >= mDataList.size() - 1)) {
                    contentViewHolder.mDivider.setVisibility(View.GONE);
                } else {
                    contentViewHolder.mDivider.setVisibility(View.VISIBLE);
                }
                contentViewHolder.bind(mDataList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = mDataList.size();
        if (mHasFooter) {
            count++;
        }
        if (mHasHeader) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeader && position == 0) {
            return TYPE_HEADER;
        } else if (mHasFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public View mCreate;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mCreate = itemView.findViewById(R.id.create);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView Tv_Hint;
        public ProgressBar Pb_Progress;
        public FooterViewHolder(View itemView) {
            super(itemView);
            Tv_Hint = (TextView) itemView.findViewById(R.id.newslist_footer_text);
            Pb_Progress = (ProgressBar) itemView.findViewById(R.id.newslist_footer_progressbar);
        }

        public void loading(){
            Pb_Progress.setVisibility(View.VISIBLE);
            Tv_Hint.setText(Tv_Hint.getContext().getString(R.string.loading));
        }

        public void complete(){
            Pb_Progress.setVisibility(View.GONE);
            Tv_Hint.setText(Tv_Hint.getContext().getString(R.string.loaded));
        }

        public void noMore(){
            Pb_Progress.setVisibility(View.GONE);
            Tv_Hint.setText(Tv_Hint.getContext().getString(R.string.no_more));
        }
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Date, Tv_Time, Tv_Content, Tv_Delete, Tv_Support, Tv_Dicuss, Tv_Share, Tv_Name;
        public NineGridImageView mPictures;
        public SimpleDraweeView mPhoto;
        public View mDivider;

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
            mDivider = itemView.findViewById(R.id.divider);
            Tv_Date = (TextView) itemView.findViewById(R.id.date);
            Tv_Time = (TextView) itemView.findViewById(R.id.time);
            Tv_Content = (TextView) itemView.findViewById(R.id.content);
            Tv_Delete = (TextView) itemView.findViewById(R.id.delete);
            Tv_Support = (TextView) itemView.findViewById(R.id.support);
            Tv_Dicuss = (TextView) itemView.findViewById(R.id.discuss);
            Tv_Share = (TextView) itemView.findViewById(R.id.share);
            Tv_Name = (TextView) itemView.findViewById(R.id.name);
            mPictures = (NineGridImageView) itemView.findViewById(R.id.pictures);
            mPhoto = (SimpleDraweeView) itemView.findViewById(R.id.photo);
            mPictures.setAdapter(mAdapter);
            mDateFormat = new DateFormat();
        }

        public void bind(Data_DB_Disclose disclose) {
            mPictures.setImagesData(Arrays.asList(disclose.getImgs().split(",")));
            Tv_Content.setText(disclose.getContent());
            String[] date = mDateFormat.format(disclose.getCreateTime());
            Tv_Date.setText(date[0]);
            Tv_Time.setText(date[1]);
            mPhoto.setImageURI(Uri.parse(disclose.getHeadurl()));
            Tv_Name.setText(disclose.getUsername());
        }
    }
}
