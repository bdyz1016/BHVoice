package com.bhsc.mobile.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bhsc.mobile.MyApplication;
import com.bhsc.mobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhanglei on 16/5/3.
 */
public class ImageViewer extends Dialog {

    private List<String> mImageSource;
    private int mPosition = 0;
    private Context mContext;

    public ImageViewer(Context context){
        super(context, R.style.FullScreenDialog);
        mContext = context;
    }

    public void setImageSource(List<String> list, int position){
        mImageSource = list;
        mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_imageviewer);
        ViewPager vp_Images = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter mAdapter = new ViewPagerAdapter(mContext);
        mAdapter.addAll(mImageSource);
        vp_Images.setAdapter(mAdapter);
        vp_Images.setCurrentItem(mPosition, false);
    }

    @Override
    public void show() {
        super.show();
    }

    private static class ViewPagerAdapter extends PagerAdapter{

        private List<View> mViewList;
        private List<String> mImageList;
        private Context mContext;
        public ViewPagerAdapter(Context context){
            mContext = context;
            mViewList = new ArrayList<>();
        }

        public void addAll(List<String> list){
            mImageList = list;
            for(int i = 0;i < list.size();i++){
                mViewList.add(LayoutInflater.from(mContext).inflate(R.layout.item_imageview, null));
            }
            notifyDataSetChanged();
        }

        public void clear(){
            mViewList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Picasso.with(mContext)
                    .load(mImageList.get(position))
                    .resize(MyApplication.screenWidth, MyApplication.screenHeight)
                    .centerInside()
                    .into(imageView);
            container.addView(view);
            return view;
        }
    }
}
