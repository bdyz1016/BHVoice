package com.bhsc.mobile.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhsc.mobile.R;
import com.bhsc.mobile.news.adapter.PagerAdapter;
import com.bhsc.mobile.news.model.Data_DB_NewsType;
import com.bhsc.mobile.utils.L;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

/**
 * Created by lynn on 15-9-17.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private View mContentView;

    private SlidingTabLayout mSlidingTabLayout;
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
        fragment_home_viewpager = (ViewPager) mContentView.findViewById(R.id.fragment_home_viewpager);
        fragment_home_search = mContentView.findViewById(R.id.fragment_home_search);
        fragment_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchActivity.class));
            }
        });
        mSlidingTabLayout = (SlidingTabLayout) mContentView.findViewById(R.id.tab);
    }

    private void initView() {
        L.i(TAG, "initView");
        String[] defaultTypes = mContext.getResources().getStringArray(R.array.news_category);
        mNewsTypeList = getNewsTypes(defaultTypes);
        mPagerAdapter = new PagerAdapter(mContext, getChildFragmentManager(), mNewsTypeList);
        fragment_home_viewpager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(fragment_home_viewpager, defaultTypes);
    }

    private void search() {
        startActivity(new Intent(mContext, SearchActivity.class));
    }

    public ArrayList<Data_DB_NewsType> getNewsTypes(String[] defaultTypes) {
        ArrayList<Data_DB_NewsType> types = new ArrayList<>();
        int length = defaultTypes.length;
        for (int i = 0; i < length; i++) {
            Data_DB_NewsType type = new Data_DB_NewsType();
            type.setTypeId(i + 1);
            type.setTypeName(defaultTypes[i]);
            types.add(type);
        }
        return types;
    }
}
