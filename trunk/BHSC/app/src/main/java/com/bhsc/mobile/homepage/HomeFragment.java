package com.bhsc.mobile.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.homepage.adapter.PagerAdapter;
import com.bhsc.mobile.utils.L;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by lynn on 15-9-17.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    @InjectAll
    Views views;

    class Views{
        public CategoryTabStrip fragment_home_category;
        public ViewPager fragment_home_viewpager;
        @InjectBinder(method = "search", listeners = {OnClick.class})
        public View fragment_home_search;
    }

    private Context mContext;

    private PagerAdapter mPagerAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.i(TAG,"onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
        Handler_Inject.injectFragment(this, rootView);
        return rootView;
    }

    @InjectInit
    private void initView(){
        L.i(TAG,"initView");
        mPagerAdapter = new PagerAdapter(mContext, getChildFragmentManager());
        views.fragment_home_viewpager.setAdapter(mPagerAdapter);
        views.fragment_home_category.setViewPager(views.fragment_home_viewpager);
    }

    private void search(){
        startActivity(new Intent(mContext, SearchActivity.class));
    }
}
