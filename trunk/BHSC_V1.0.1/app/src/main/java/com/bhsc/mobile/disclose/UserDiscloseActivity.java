package com.bhsc.mobile.disclose;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bhsc.mobile.disclose.adapter.DiscloseRecyclerAdapter;
import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.mobile.userpages.LoginAndRegisterActivity;
import com.bhsc.mobile.userpages.UserManager;
import com.bhsc.mobile.userpages.dialog.DefaultDialog;
import com.bhsc.mobile.utils.L;

import java.util.List;

/**
 * Created by zhanglei on 16/4/21.
 */
public class UserDiscloseActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, SwipeRefreshLayout.OnRefreshListener, DiscloseManager.OnDiscloseListener, DiscloseRecyclerAdapter.OnRequestToLoadMoreListener, DiscloseRecyclerAdapter.OnItemLongClickListener {
    private final String TAG = UserDiscloseActivity.class.getSimpleName();

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton Fab_CreateDisclose;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DiscloseRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DiscloseManager mManager;

    private ProgressDialog mProgressDialog;

    private boolean isTitleCollapsed = false;
    private boolean isSwipeRefreshEnable = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_disclose);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initWidget();
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        mManager.refresh(UserManager.getUser().getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
    }

    private void initWidget(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Fab_CreateDisclose = (FloatingActionButton) findViewById(R.id.fab);
        Fab_CreateDisclose.setOnClickListener(mOnCreateDiscloseListener);
        mRecyclerView = (RecyclerView) findViewById(R.id.disclose_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initData(){
        mAdapter = new DiscloseRecyclerAdapter(this);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.hasFooter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mManager = new DiscloseManager(this, this);
    }

    private void initView(){
        mManager.loadDataFromLocal(UserManager.getUser().getId());
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
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    @Override
    public void deleteSuccess() {
        dismissProgressDialog();
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(true);
        mManager.refresh(UserManager.getUser().getId());
    }

    @Override
    public void error(int error) {
        if(error > 0 && error < 0x10){
            mAdapter.setState(DiscloseRecyclerAdapter.STATE_NO_MORE);
        } else if(error >= 10 && error < 0x20){
            mSwipeRefreshLayout.setRefreshing(false);
        } else if(error >= 0x20){
            dismissProgressDialog();
            Toast.makeText(this, "删除失败:" + error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadMoreRequested() {
        mManager.loadDataFromCloud(UserManager.getUser().getId());
    }

    @Override
    public void onRefresh() {
        mManager.refresh(UserManager.getUser().getId());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        L.i(TAG, "onOffsetChanged:" + verticalOffset);
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        L.i(TAG, "percentage:" + percentage);
        if (percentage > PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR && !isTitleCollapsed) {
            isTitleCollapsed = true;
            mToolbar.setNavigationIcon(R.mipmap.btn_goback);
        } else if (percentage < PERCENTAGE_TO_HIDE_TITLE_DETAILS && isTitleCollapsed) {
            isTitleCollapsed = false;
            mToolbar.setNavigationIcon(R.mipmap.btn_back);
        }
        if(isSwipeRefreshEnable && verticalOffset < 0){
            isSwipeRefreshEnable = false;
            mSwipeRefreshLayout.setEnabled(false);
        } else if(verticalOffset == 0 && !isSwipeRefreshEnable) {
            isSwipeRefreshEnable = true;
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void onItemLongClick(View view, int position, final long id) {
        final DefaultDialog dialog = new DefaultDialog(this);
        dialog.setMessage("确定删除爆料?");
        dialog.setOnNegativeClickListener(new DefaultDialog.OnButtonClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
                displayProgressDialog("正在删除...");
                mManager.deleteDisclose(id);
            }
        });
        dialog.show();
    }

    private void displayProgressDialog(String message){
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        if(!TextUtils.isEmpty(message)){
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    private void dismissProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
