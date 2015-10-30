package com.bhsc.mobile.disclose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.ShareActivity;
import com.bhsc.mobile.datalcass.Data_DB_Disclose;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.SyncArrayList;
import com.bhsc.mobile.view.dialog.DefaultDialog;

import java.util.LinkedList;

/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseFragment extends Fragment {
    private final String TAG = DiscloseFragment.class.getSimpleName();

    class Views {
        @InjectBinder(method = "createDisclose", listeners = {OnClick.class})
        public View fragment_disclose_create;
        public MyListView fragment_disclose_list;
    }

    private View mContentView;

    private Context mContext;

    private SyncArrayList<Data_DB_Disclose> mDataList = new SyncArrayList<>(new LinkedList<Data_DB_Disclose>());

    private DiscloseAdapter mAdapter;

    private DisclosePresenter mDisclosePresenter;

    private DefaultDialog mDialog;

    private int mDeletedPosition;
    @InjectAll
    Views views;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.fragment_disclose, container, false);
            Handler_Inject.injectFragment(this, mContentView);
        }
        return mContentView;
    }

    @InjectInit
    private void init() {
        mDisclosePresenter = DisclosePresenter.getInstance(mContext);
        mAdapter = new DiscloseAdapter(mContext, mDataList, new DiscloseAdapter.OnElementClickListener() {
            @Override
            public void onDelete(final int position) {
                L.i(TAG, "onDelete");
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mDialog = new DefaultDialog(mContext);
                mDialog.setMessage("确定删除?");
                mDialog.setOnPositiveClickListener(new DefaultDialog.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        mDeletedPosition = position;
                        mDisclosePresenter.deleteDisclose(mDataList.get(position));
                        mDialog.dismiss();
                    }
                });
                mDialog.setOnNegativeClickListener(new DefaultDialog.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();
            }

            @Override
            public void onShare(int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, ShareActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSupport(int position) {
            }
        });
        views.fragment_disclose_list.setAdapter(mAdapter);
        mDisclosePresenter.getAllDisclose();
    }

    private void createDisclose() {
        Intent intent = new Intent();
        intent.setClass(mContext, DiscloseActivity.class);
        mContext.startActivity(intent);
    }

    public void onEventMainThread(ActionEvent event) {
        if (event.getAction() == ActionEvent.ACTION_LOAD_DISCLOSE) {
            L.i(TAG, "load disclose");
            mDataList.addAll(event.getDiscloseList());
            mAdapter.notifyDataSetChanged();
        } else if (event.getAction() == ActionEvent.ACTION_DISCLOSE_DELETE_SUCCESS) {
            mDataList.remove(mDeletedPosition);
            mAdapter.notifyDataSetChanged();
        } else if(event.getAction() == ActionEvent.ACTION_ADD_DISCLOSE_FINISH){
            mDisclosePresenter.getAllDisclose();
        }
    }
}
