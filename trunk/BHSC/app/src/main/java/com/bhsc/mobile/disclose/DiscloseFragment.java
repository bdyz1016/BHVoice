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

import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Inject;
import com.bhsc.mobile.R;
import com.bhsc.mobile.disclose.views.MyListView;

/**
 * Created by lynn on 15-9-29.
 */
public class DiscloseFragment extends Fragment {

    private View mContentView;

    private Context mContext;

    @InjectAll
    Views views;

    class Views{
        @InjectBinder(method = "createDisclose", listeners = {OnClick.class})
        public View fragment_disclose_create;
        public MyListView fragment_disclose_list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null) {
            mContentView = inflater.inflate(R.layout.fragment_disclose, container, false);
            Handler_Inject.injectFragment(this, mContentView);
        }
        return mContentView;
    }

    private void createDisclose(){
        Intent intent = new Intent();
        intent.setClass(mContext, DiscloseActivity.class);
        mContext.startActivity(intent);
    }
}
