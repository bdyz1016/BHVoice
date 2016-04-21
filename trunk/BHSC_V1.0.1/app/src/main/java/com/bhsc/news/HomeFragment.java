package com.bhsc.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhsc.mobile.R;
import com.bhsc.news.adapter.PagerAdapter;
import com.bhsc.news.model.Data_DB_NewsType;
import com.bhsc.utils.L;

import java.util.ArrayList;

/**
 * Created by lynn on 15-9-17.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private View mContentView;

    private TabLayout tablayout;
    private ViewPager fragment_home_viewpager;
    private View fragment_home_search;
    private Context mContext;

    private PagerAdapter mPagerAdapter;

    private ArrayList<Data_DB_NewsType> mNewsTypeList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.i(TAG, "onCreateView");
        mContentView = inflater.inflate(R.layout.fragment_homepage, container, false);
        initWidget();
        initView();
        return mContentView;
    }

    private void initWidget(){
        tablayout = (TabLayout) mContentView.findViewById(R.id.tablayout);
        fragment_home_viewpager = (ViewPager) mContentView.findViewById(R.id.fragment_home_viewpager);
        fragment_home_search = mContentView.findViewById(R.id.fragment_home_search);
    }

    private void initView() {
        L.i(TAG, "initView");

        mNewsTypeList = getNewsTypes();
        mPagerAdapter = new PagerAdapter(mContext, getChildFragmentManager(), mNewsTypeList);
        fragment_home_viewpager.setAdapter(mPagerAdapter);
        tablayout.setupWithViewPager(fragment_home_viewpager);
    }

    private void search() {
        startActivity(new Intent(mContext, SearchActivity.class));
    }

    public ArrayList<Data_DB_NewsType> getNewsTypes() {
        ArrayList<Data_DB_NewsType> types = new ArrayList<>();
        String[] defaultTypes = mContext.getResources().getStringArray(R.array.news_category);
        int length = defaultTypes.length;
        for (int i = 1; i < length; i++) {
            Data_DB_NewsType type = new Data_DB_NewsType();
            type.setTypeId(i);
            type.setTypeName(defaultTypes[i]);
            types.add(type);
        }
        return types;
    }
}
