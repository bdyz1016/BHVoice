package com.bhsc.disclose;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bhsc.disclose.adapter.DiscloseRecyclerAdapter;
import com.bhsc.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.userpages.LoginAndRegisterActivity;
import com.bhsc.userpages.UserManager;
import com.bhsc.utils.L;

import java.util.List;

/**
 * Created by zhanglei on 16/4/21.
 */
public class UserDiscloseActivity extends AppCompatActivity implements DiscloseManager.OnDiscloseListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton Fab_CreateDisclose;
    private RecyclerView mRecyclerView;
    private DiscloseRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DiscloseManager mManager;

    private boolean isTitleCollapsed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_disclose);
        initWidget();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
    }

    private void initWidget(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Fab_CreateDisclose = (FloatingActionButton) findViewById(R.id.fab);
        Fab_CreateDisclose.setOnClickListener(mOnCreateDiscloseListener);
        mRecyclerView = (RecyclerView) findViewById(R.id.disclose_list);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                L.i("zahnglei", "percentage:" + percentage);
                if(percentage > PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR && !isTitleCollapsed){
                    isTitleCollapsed = true;
                    mToolbar.setNavigationIcon(R.mipmap.btn_goback);
                } else if(percentage < PERCENTAGE_TO_HIDE_TITLE_DETAILS && isTitleCollapsed){
                    isTitleCollapsed = false;
                    mToolbar.setNavigationIcon(R.mipmap.btn_back);
                }
            }
        });
    }

    private void initData(){
        mAdapter = new DiscloseRecyclerAdapter();
        mAdapter.hasFooter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mManager = new DiscloseManager(this, this);
        mManager.loadData();
    }

    private View.OnClickListener mOnCreateDiscloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(UserManager.isLogin()){
                startActivity(new Intent(UserDiscloseActivity.this, DiscloseActivity.class));
            } else {
                startActivity(new Intent(UserDiscloseActivity.this, LoginAndRegisterActivity.class));
            }
        }
    };

    @Override
    public void onLoaded(List<Data_DB_Disclose> list) {
        mAdapter.addAll(list);
    }

    @Override
    public void onRefreshed(List<Data_DB_Disclose> list) {
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    @Override
    public void error(int error) {

    }
}
