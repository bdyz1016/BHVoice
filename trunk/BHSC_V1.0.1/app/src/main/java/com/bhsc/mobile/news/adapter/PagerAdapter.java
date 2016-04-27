package com.bhsc.mobile.news.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bhsc.mobile.news.NewsFragment;
import com.bhsc.mobile.news.model.Data_DB_NewsType;
import com.bhsc.mobile.utils.L;

import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final String TAG = PagerAdapter.class.getSimpleName();

    private int mNewsTypeCount;

    private List<Data_DB_NewsType> mCategorys;

    public PagerAdapter(Context context, FragmentManager fm, List<Data_DB_NewsType> categorys){
        super(fm);
        L.i(TAG, "PagerAdapter");
        mCategorys =categorys;
        mNewsTypeCount = mCategorys.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        L.i(TAG, "getPageTitle:" + position);
        return mCategorys.get(position).getTypeName();
    }

    @Override
    public int getCount() {
        return mCategorys.size();
    }

    @Override
    public Fragment getItem(int position) {
        L.i(TAG, "getItem:" + position);
        NewsFragment fragment = new NewsFragment();
        fragment.setNewsType(mCategorys.get(position).getTypeId());
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        return mCategorys.get(position).getTypeId();
    }
}
