package com.bhsc.mobile.disclose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhsc.mobile.disclose.adapter.DiscloseRecyclerAdapter;
import com.bhsc.mobile.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.mobile.userpages.LoginAndRegisterActivity;
import com.bhsc.mobile.userpages.UserManager;
import com.bhsc.mobile.utils.L;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

/**
 * Created by zhanglei on 16/4/17.
 */
public class DiscloseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DiscloseRecyclerAdapter.OnRequestToLoadMoreListener{
    private final String TAG = DiscloseFragment.class.getSimpleName();
    private final int SCROLL_OFFSET = 4;

    private View mContentView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionMenu mFloatActionMenu;
    private FloatingActionButton Fab_Create;
    private FloatingActionButton Fab_MyDisclose;
    private DiscloseRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private DiscloseManager mDiscloseManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_disclose, container, false);
        initWidget();
        initData();
        initView();
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        mDiscloseManager.refresh(-1);
    }

    private void initWidget(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.category_tab_highlight_bg);

        mFloatActionMenu = (FloatingActionMenu) mContentView.findViewById(R.id.float_action_menu);
        Fab_Create = (FloatingActionButton) mContentView.findViewById(R.id.fab_create_disclose);
        Fab_Create.setOnClickListener(mOnCreateDiscloseListener);
        Fab_MyDisclose = (FloatingActionButton) mContentView.findViewById(R.id.fab_my_disclose);
        Fab_MyDisclose.setOnClickListener(mOnMyDiscloseListener);

        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.disclose_list);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DiscloseRecyclerAdapter(this);
        mAdapter.hasFooter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_OFFSET) {
                    if (dy > 0) {
                        mFloatActionMenu.close(true);
                        mFloatActionMenu.hideMenu(true);
                    } else {
                        mFloatActionMenu.showMenu(true);
                    }
                }
            }
        });
    }

    private void initData(){
        mDiscloseManager = new DiscloseManager(mContext, new DiscloseManager.OnDiscloseListener() {
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

            }

            @Override
            public void error(int error) {
                if(error > 0 && error < 0x10){
                    mAdapter.setState(DiscloseRecyclerAdapter.STATE_NO_MORE);
                } else if(error >= 10 && error < 0x20){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initView(){
        mDiscloseManager.loadDataFromLocal(-1);
    }

    private View.OnClickListener mOnCreateDiscloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFloatActionMenu.close(true);
            if(UserManager.isLogin()){
                startActivity(new Intent(mContext, DiscloseActivity.class));
            } else {
                startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
            }
        }
    };

    private View.OnClickListener mOnMyDiscloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFloatActionMenu.close(true);
            if(UserManager.isLogin()){
                startActivity(new Intent(mContext, UserDiscloseActivity.class));
            } else {
                startActivity(new Intent(mContext, LoginAndRegisterActivity.class));
            }
        }
    };

    @Override
    public void onRefresh() {
        mDiscloseManager.refresh(-1);
    }

    @Override
    public void loadMoreRequested() {
        L.i(TAG, "loadMoreRequested");
        mDiscloseManager.loadDataFromCloud(-1);
    }
}
