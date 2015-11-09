package com.bhsc.mobile.disclose;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.ThirdParty.ShareActivity;
import com.bhsc.mobile.dataclass.Data_DB_Disclose;
import com.bhsc.mobile.disclose.adapter.DiscloseAdapter;
import com.bhsc.mobile.disclose.event.ActionEvent;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.manager.UserManager;
import com.bhsc.mobile.userpages.LoginAndRegisterActivity;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.SyncArrayList;
import com.bhsc.mobile.view.dialog.DefaultDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseFragment extends Fragment {
    private final String TAG = DiscloseFragment.class.getSimpleName();

    class Views {
        @InjectBinder(method = "createDisclose", listeners = {OnClick.class})
        public View fragment_disclose_create;
        public MyListView fragment_disclose_list;
        public RelativeLayout fragment_disclose_container;
    }

    private View mContentView;

    private View mDiscloseGuide;

    private Context mContext;

    private SyncArrayList<Data_DB_Disclose> mDataList = new SyncArrayList<>(new LinkedList<Data_DB_Disclose>());

    private DiscloseAdapter mAdapter;

    private DisclosePresenter mDisclosePresenter;

    private DefaultDialog mDialog;

    private IntroductionsDialog mIntroductionsDialog;

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
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
        if(mIntroductionsDialog != null && mIntroductionsDialog.isShowing()){
            mDialog.dismiss();
        }
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
        L.i(TAG, "init");
        mDisclosePresenter = DisclosePresenter.getInstance(mContext);
        mDataList.clear();
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
                mDisclosePresenter.support(mDataList.get(position).getId());
            }

            @Override
            public void onComment(int position) {

            }
        });
        views.fragment_disclose_list.setAdapter(mAdapter);
        mDisclosePresenter.getAllDisclose();
        addDiscloseGuideView();
    }

    private void createDisclose() {
        Intent intent = new Intent();
        intent.setClass(mContext, DiscloseListActivity.class);
        mContext.startActivity(intent);
    }

    public void onEventMainThread(ActionEvent event) {
        if (event.getAction() == ActionEvent.ACTION_LOAD_DISCLOSE) {
            L.i(TAG, "load disclose");
            List<Data_DB_Disclose> list = event.getDiscloseList();
            if(list != null && list.size() > 0) {
                removeDiscloseGuideView();
                mDataList.clear();
                mDataList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        } else if (event.getAction() == ActionEvent.ACTION_DISCLOSE_DELETE_SUCCESS) {
            mDataList.remove(mDeletedPosition);
            mAdapter.notifyDataSetChanged();
        } else if (event.getAction() == ActionEvent.ACTION_ADD_DISCLOSE_FINISH) {
            L.i(TAG, "add disclose");
            mDisclosePresenter.getAllDisclose();
        } else if (event.getAction() == ActionEvent.ACTION_PRAISE_SUCCESS) {
            mDisclosePresenter.getAllDisclose();
        }
    }

    private void addDiscloseGuideView() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        mDiscloseGuide = inflater.inflate(R.layout.view_disclose_direction, null, true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mDiscloseGuide.setLayoutParams(layoutParams);
        views.fragment_disclose_container.addView(mDiscloseGuide);

        View guide = mDiscloseGuide.findViewById(R.id.view_disclose_direction_guide);
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIntroductionsDialog != null && mIntroductionsDialog.isShowing()){
                    mIntroductionsDialog.dismiss();
                }
                mIntroductionsDialog = new IntroductionsDialog(mContext);
                mIntroductionsDialog.show();
            }
        });

        Button editDisclose = (Button) mDiscloseGuide.findViewById(R.id.view_disclose_direction_edit);
        editDisclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserManager.getInstance(mContext).isLogined()){
                    Intent intent = new Intent();
                    intent.setClass(mContext, DiscloseActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LoginAndRegisterActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void removeDiscloseGuideView(){
        if(mDiscloseGuide != null) {
            views.fragment_disclose_container.removeView(mDiscloseGuide);
        }
    }

    private class IntroductionsDialog extends AlertDialog{

        private IntroductionsDialog(Context context){
            super(context, R.style.DefaultDialogStyle);
        }

        private Button Btn_Confirm;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_disclose_introductions);
            Btn_Confirm = (Button) findViewById(R.id.dialog_alert_positive);
            Btn_Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntroductionsDialog.this.dismiss();
                }
            });
        }
    }
}
