package com.dp2px;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lynn on 15-9-29.
 */
public class Dp2pxFragment extends Fragment implements View.OnClickListener {

    private View mContentView;
    private TextView Tv_Result, Tv_px2dp, Tv_dp2px;
    private EditText Edit_Input;

    private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_dp2px, container, false);
        initWidget();
        return mContentView;
    }

    private void initWidget(){

        Tv_Result = (TextView) mContentView.findViewById(R.id.result);
        Tv_px2dp = (TextView) mContentView.findViewById(R.id.px2dp);
        Tv_dp2px = (TextView) mContentView.findViewById(R.id.dp2px);
        Edit_Input = (EditText) mContentView.findViewById(R.id.input);

        Tv_px2dp.setOnClickListener(this);
        Tv_dp2px.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String input = Edit_Input.getText().toString();
        switch(v.getId()){
            case R.id.px2dp:
                if(!TextUtils.isEmpty(input)){
                    Tv_Result.setText(px2dip(mContext, Float.parseFloat(input)) + "");
                }
                break;
            case R.id.dp2px:
                if(!TextUtils.isEmpty(input)){
                    Tv_Result.setText(dip2px(mContext, Float.parseFloat(input)) + "");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
