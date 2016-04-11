package com.bhsc.disclose;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bhsc.disclose.adapter.DiscloseRecyclerAdapter;
import com.bhsc.disclose.model.Data_DB_Disclose;
import com.bhsc.mobile.R;
import com.bhsc.utils.L;
import com.bhsc.utils.SyncArrayList;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseFragment extends Fragment implements DiscloseManager.OnDiscloseListener {
    private final String TAG = DiscloseFragment.class.getSimpleName();

//    class Views {
//        @InjectBinder(method = "createDisclose", listeners = {OnClick.class})
//        public View fragment_disclose_create;
//        public MyListView fragment_disclose_list;
//        public RelativeLayout fragment_disclose_container;
//    }

    private final LinkedList<Data_DB_Disclose> mDataList = new LinkedList<>();

    private View mContentView;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton mCreateDisclose;

    private DiscloseRecyclerAdapter mAdapter;

    private Context mContext;

    private DiscloseManager mDiscloseManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.fragment_disclose, container, false);
        }
        initWidget();
        initData();
        return mContentView;
    }

    private void initWidget() {
        L.i(TAG, "initWidget");
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.disclose_list);
        mCreateDisclose = (FloatingActionButton) mContentView.findViewById(R.id.create_disclose);
        mCreateDisclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DiscloseActivity.class));
            }
        });
    }

    private void initData() {
        L.i(TAG, "initData");
        mDiscloseManager = new DiscloseManager(mContext, this);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DiscloseRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.LIST_VERTICAL));
        mDiscloseManager.loadData();
    }

    private void createDisclose() {
//        Intent intent = new Intent();
//        intent.setClass(mContext, DiscloseListActivity.class);
//        mContext.startActivity(intent);
    }

    @Override
    public void loaded(List<Data_DB_Disclose> list) {
        mDataList.addAll(list);
        mAdapter.addAll(list);
    }


//    private void addDiscloseGuideView() {
//        final LayoutInflater inflater = LayoutInflater.from(mContext);
//        mDiscloseGuide = inflater.inflate(R.layout.view_disclose_direction, null, true);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        mDiscloseGuide.setLayoutParams(layoutParams);
//        views.fragment_disclose_container.addView(mDiscloseGuide);
//
//        View guide = mDiscloseGuide.findViewById(R.id.view_disclose_direction_guide);
//        guide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mIntroductionsDialog != null && mIntroductionsDialog.isShowing()){
//                    mIntroductionsDialog.dismiss();
//                }
//                mIntroductionsDialog = new IntroductionsDialog(mContext);
//                mIntroductionsDialog.show();
//            }
//        });
//
//        Button editDisclose = (Button) mDiscloseGuide.findViewById(R.id.view_disclose_direction_edit);
//        editDisclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(UserManager.getInstance(mContext).isLogined()){
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, DiscloseActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, LoginAndRegisterActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
//    }

    private class IntroductionsDialog extends AlertDialog {

        private IntroductionsDialog(Context context) {
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
