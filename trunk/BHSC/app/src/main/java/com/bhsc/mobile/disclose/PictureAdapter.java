package com.bhsc.mobile.disclose;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.media.ImageUtil;
import com.bhsc.mobile.utils.L;

import java.util.List;

/**
 * Created by lynn on 10/17/15.
 */
public class PictureAdapter extends ArrayAdapter<String> {
    private final String TAG = PictureAdapter.class.getSimpleName();

    public static final String DEFAULT_IMAGE = "default";

    private LayoutInflater mInflater;
    private int mResource;
    private Context mContext;

    private OnAddPictureListener mListener;

    public PictureAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.Iv_Image = (ImageView) convertView.findViewById(R.id.picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String picture = getItem(position);

        L.i(TAG, "position:" + position + " ,picture path:" + picture);
        if(DEFAULT_IMAGE.equals(picture)){
            holder.Iv_Image.setImageResource(R.mipmap.btn_add_image);
        } else {
            ImageUtil.getInstance().loadBitmap(picture, holder.Iv_Image, 100, 100);
        }
        return convertView;
    }

    public void setOnAddPictureListener(OnAddPictureListener listener){
        this.mListener = listener;
    }

    private class ViewHolder {
        public ImageView Iv_Image;
    }

    public interface OnAddPictureListener{
        void addPicture();
    }

    private TextWatcher mTitleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
