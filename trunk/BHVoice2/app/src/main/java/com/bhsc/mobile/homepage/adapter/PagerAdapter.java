package com.bhsc.mobile.homepage.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bhsc.mobile.R;
import com.bhsc.mobile.homepage.NewsFragment;
import com.bhsc.mobile.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lynn on 15-9-17.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final String TAG = PagerAdapter.class.getSimpleName();

    private int mNewsTypeCount;

    private final List<String> mCategorys = new ArrayList<String>();
//    private NewsFragment[] mFragments;

    public PagerAdapter(Context context, FragmentManager fm) {
        this(context, fm, null);
        L.i(TAG, "PagerAdapter");
    }

    public PagerAdapter(Context context, FragmentManager fm, List<String> categorys){
        super(fm);
        L.i(TAG, "PagerAdapter");
        mCategorys.addAll(Arrays.asList(context.getResources().getStringArray(R.array.news_category)));
        if(categorys != null && categorys.size() > 0){
            mCategorys.addAll(categorys);
        }
        mNewsTypeCount = mCategorys.size();
//        mFragments = new NewsFragment[mNewsTypeCount];
//        for(int i = 0;i < mNewsTypeCount; i++){
//            mFragments[i] = new NewsFragment();
//        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        L.i(TAG, "getPageTitle:" + position);
        return mCategorys.get(position);
    }

    @Override
    public int getCount() {
        L.i(TAG, "getCount:" + mCategorys.size());
        return mCategorys.size();
    }

    @Override
    public Fragment getItem(int position) {
        L.i(TAG, "getItem:" + position);
        return new NewsFragment();
    }
}
