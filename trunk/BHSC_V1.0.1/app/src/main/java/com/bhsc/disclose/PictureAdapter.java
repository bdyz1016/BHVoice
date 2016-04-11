//package com.bhsc.disclose;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import com.bhsc.mobile.R;
//import com.bhsc.mobile.media.ImageUtil;
//import com.bhsc.mobile.utils.L;
//
//import java.util.List;
//
///**
// * Created by lynn on 10/17/15.
// */
//public class PictureAdapter extends ArrayAdapter<String> {
//    private final String TAG = PictureAdapter.class.getSimpleName();
//
//    public static final String DEFAULT_IMAGE = "default";
//
//    public static final int UN_EDITABLE = -1;
//
//    private LayoutInflater mInflater;
//    private int mResource;
//    private Context mContext;
//
//    private int mEditPosition = -1;
//
//    private OnAddPictureListener mListener;
//
//    public PictureAdapter(Context context, int resource, List<String> objects) {
//        super(context, resource, objects);
//        this.mInflater = LayoutInflater.from(context);
//        this.mResource = resource;
//        this.mContext = context;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(mResource, parent, false);
//            holder = new ViewHolder();
//            holder.Iv_Image = (ImageView) convertView.findViewById(R.id.picture);
//            holder.Btn_Detele = (Button) convertView.findViewById(R.id.delete);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        final String picture = getItem(position);
//
//        L.i(TAG, "position:" + position + " ,picture path:" + picture);
//        if(DEFAULT_IMAGE.equals(picture)){
//            holder.Iv_Image.setImageResource(R.mipmap.btn_add_image);
//        } else {
//            ImageUtil.getInstance().loadBitmap(picture, holder.Iv_Image, 100, 100);
//        }
//        if(mEditPosition >= 0 && mEditPosition == position){
//            L.i(TAG, "position:" + position + ",EditPosition:" + mEditPosition);
//            holder.Btn_Detele.setVisibility(View.VISIBLE);
//        } else {
//            holder.Btn_Detele.setVisibility(View.GONE);
//        }
//        holder.Btn_Detele.setFocusable(false);
//        holder.Btn_Detele.setClickable(false);
//        return convertView;
//    }
//
//    public void setOnAddPictureListener(OnAddPictureListener listener){
//        this.mListener = listener;
//    }
//
//    public void setEdit(int position){
//        L.i(TAG, "setEdit:" + position);
//        mEditPosition = position;
//        notifyDataSetChanged();
//    }
//
//    public int getEditPostion(){
//        return mEditPosition;
//    }
//
//    public boolean isEditable(){
//        if(mEditPosition < 0){
//            return false;
//        }
//        return true;
//    }
//
//    private class ViewHolder {
//        public ImageView Iv_Image;
//        public Button Btn_Detele;
//    }
//
//    public interface OnAddPictureListener{
//        void addPicture();
//    }
//
//}
