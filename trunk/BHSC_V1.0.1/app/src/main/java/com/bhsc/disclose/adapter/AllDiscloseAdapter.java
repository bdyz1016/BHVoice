//package com.bhsc.disclose.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bhsc.mobile.R;
//import com.bhsc.mobile.dataclass.Data_DB_Disclose;
//import com.bhsc.mobile.media.ImageUtil;
//import com.bhsc.mobile.utils.L;
//import com.bhsc.mobile.utils.Method;
//import com.bhsc.mobile.utils.SyncArrayList;
//
//import java.util.List;
//
///**
// * Created by lynn on 11/3/15.
// */
//public class AllDiscloseAdapter extends BaseAdapter {
//
//    private final String TAG = DiscloseAdapter.class.getSimpleName();
//
//    public interface OnElementClickListener{
//        void onMenu(int position);
//        void onShare(int position);
//        void onSupport(int position);
//        void onComment(int position);
//    }
//
//    private SyncArrayList<Data_DB_Disclose> mDataList;
//    private Context mContext;
//    private LayoutInflater mInflater;
//    private OnElementClickListener mListener;
//
//    public AllDiscloseAdapter(Context context, SyncArrayList<Data_DB_Disclose> dataList, OnElementClickListener listener){
//        this.mContext = context;
//        this.mDataList = dataList;
//        this.mListener = listener;
//        this.mInflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return mDataList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mDataList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if(convertView == null){
//            convertView = mInflater.inflate(R.layout.item_disclose_all, parent, false);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder)convertView.getTag();
//        }
//        Data_DB_Disclose disclose = mDataList.get(position);
//        holder.Tv_Content.setText(disclose.getContent());
//        holder.Tv_Date.setText(Method.getTime(disclose.getCreateTime()));
//        GridAdapter adapter = new GridAdapter(mContext, R.layout.item_disclose_picture, disclose.getImagePaths());
//        holder.Gv_Pictures.setAdapter(adapter);
//        holder.Tv_CommentCount.setText("(" + disclose.getCommentCount() + ")");
//        holder.Tv_PraiseCount.setText("(" + disclose.getPraiseCount() + ")");
//        holder.Menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i(TAG, "delete onClick");
//                mListener.onMenu(position);
//            }
//        });
//        holder.Share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i(TAG, "Share onClick");
//                mListener.onShare(position);
//            }
//        });
//        holder.Support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i(TAG, "Support onClick");
//                mListener.onSupport(position);
//            }
//        });
//        return convertView;
//    }
//
//    private class ViewHolder{
//        private TextView Tv_Date, Tv_Time, Tv_Content, Tv_PraiseCount, Tv_CommentCount;
//        private View Menu, Support, Share;
//        private GridView Gv_Pictures;
//        public ViewHolder(View view){
//            Tv_Date = (TextView)view.findViewById(R.id.item_disclose_date);
//            Tv_Time = (TextView)view.findViewById(R.id.item_disclose_time);
//            Tv_Content = (TextView)view.findViewById(R.id.item_disclose_content);
//            Menu = view.findViewById(R.id.item_disclose_delete);
//            Gv_Pictures = (GridView) view.findViewById(R.id.item_disclose_pictures);
//            Support = view.findViewById(R.id.item_disclose_btn_support);
//            Share = view.findViewById(R.id.item_disclose_btn_share);
//            Tv_PraiseCount = (TextView) view.findViewById(R.id.item_disclose_praisecount);
//            Tv_CommentCount = (TextView) view.findViewById(R.id.item_disclose_commentcount);
//        }
//    }
//
//    private class GridAdapter extends ArrayAdapter<String> {
//        private final String TAG = GridAdapter.class.getSimpleName();
//
//        public static final String DEFAULT_IMAGE = "default";
//
//        private LayoutInflater mInflater;
//        private int mResource;
//        private Context mContext;
//
//        public GridAdapter(Context context, int resource, List<String> objects) {
//            super(context, resource, objects);
//            this.mInflater = LayoutInflater.from(context);
//            this.mResource = resource;
//            this.mContext = context;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = mInflater.inflate(mResource, parent, false);
//                holder = new ViewHolder();
//                holder.Iv_Image = (ImageView) convertView.findViewById(R.id.picture);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            final String picture = getItem(position);
//
//            L.i(TAG, "position:" + position + " ,picture path:" + picture);
//            if(DEFAULT_IMAGE.equals(picture)){
//                holder.Iv_Image.setImageResource(R.mipmap.btn_add_image);
//            } else {
//                ImageUtil.getInstance().loadBitmap(picture, holder.Iv_Image, 100, 100);
//            }
//            return convertView;
//        }
//
//        private class ViewHolder {
//            public ImageView Iv_Image;
//        }
//    }
//}
