package com.bhsc.mobile.disclose.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.comment.CommentActivity;
import com.bhsc.mobile.comment.CommentManager;
import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.DateFormat;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.views.ImageViewer;
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
    private final String TAG = DiscloseRecyclerAdapter.class.getSimpleName();

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

    private OnRequestToLoadMoreListener mListener = new OnRequestToLoadMoreListener() {
        @Override
        public void loadMoreRequested() {

        }
    };

    private OnItemLongClickListener mItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int position, long id) {

        }
    };

    public DiscloseRecyclerAdapter(OnRequestToLoadMoreListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClickListener(@NonNull OnItemLongClickListener listener){
        mItemLongClickListener = listener;
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
        L.i(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HEADER: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_disclose, parent, false);
                viewHolder = new HeaderViewHolder(contentView);
                break;
            }
            case TYPE_CONTENT: {
                View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disclose, parent, false);
                viewHolder = new ContentViewHolder(contentView, mItemLongClickListener);
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
        L.i(TAG, "onBindViewHolder:" + position);
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
                        mListener.loadMoreRequested();
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
                mCurrentState = STATE_LOADING;
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

    private static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private final String TAG = ContentViewHolder.class.getSimpleName();
        public TextView Tv_Date, Tv_Time, Tv_Content, Tv_Support, Tv_Dicuss, Tv_Share, Tv_Name;
        public NineGridImageView mPictures;
        public SimpleDraweeView mPhoto;
        public View mDivider;

        private long mDataId;
        private OnItemLongClickListener mListener;

        private DateFormat mDateFormat;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Picasso.with(context)
                        .load(s)
                        .config(Bitmap.Config.RGB_565)
                        .resize(MyApplication.DISCLOSE_IMAGE_RESIZE,MyApplication.DISCLOSE_IMAGE_RESIZE).centerCrop()
                        .placeholder(R.color.background_color)
                        .into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return new ImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
//                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
                ImageViewer imageViewer = new ImageViewer(context);
                imageViewer.setImageSource(list, index);
                imageViewer.show();
            }
        };

        public ContentViewHolder(View itemView, OnItemLongClickListener listener) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            mDivider = itemView.findViewById(R.id.divider);
            Tv_Date = (TextView) itemView.findViewById(R.id.date);
            Tv_Time = (TextView) itemView.findViewById(R.id.time);
            Tv_Content = (TextView) itemView.findViewById(R.id.content);
            Tv_Support = (TextView) itemView.findViewById(R.id.support);
            Tv_Dicuss = (TextView) itemView.findViewById(R.id.discuss);
            Tv_Share = (TextView) itemView.findViewById(R.id.share);
            Tv_Name = (TextView) itemView.findViewById(R.id.name);
            mPictures = (NineGridImageView) itemView.findViewById(R.id.pictures);
            mPhoto = (SimpleDraweeView) itemView.findViewById(R.id.photo);
            mPictures.setAdapter(mAdapter);
            mDateFormat = new DateFormat();
            mListener = listener;
        }

        public void bind(final Data_DB_Disclose disclose) {
            L.i(TAG, "bind");
            if(!TextUtils.isEmpty(disclose.getImgs())) {
                mPictures.setImagesData(Arrays.asList(disclose.getImgs().split(",")));
            } else {
                mPictures.setVisibility(View.GONE);
            }
            Tv_Content.setText(disclose.getContent());
            StringBuilder dateStringBuilder = new StringBuilder("");
            String[] dateArray = mDateFormat.format(disclose.getCreateTime());
            for(String s:dateArray){
                if(!TextUtils.isEmpty(s)) {
                    dateStringBuilder.append(s).append(" ");
                }
            }
            Tv_Date.setText(dateStringBuilder.toString());
            mPhoto.setImageURI(Uri.parse(disclose.getHeadurl()));
            Tv_Name.setText(disclose.getUsername());
            Tv_Dicuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CommentActivity.class);
                    intent.putExtra(CommentActivity.INTENT_FATHER_ID, disclose.getId());
                    intent.putExtra(CommentActivity.INTENT_COMMENT_TYPE, CommentManager.TYPE_DISCLOSE);
                    v.getContext().startActivity(intent);
                }
            });
            mDataId = disclose.getId();
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongClick(v, getAdapterPosition(), mDataId);
            return true;
        }
    }

    public interface OnRequestToLoadMoreListener {
        /**
         * The adapter requests to load more data.
         * load after
         */
        void loadMoreRequested();
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position, long id);
    }
}
