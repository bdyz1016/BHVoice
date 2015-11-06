package com.bhsc.mobile.discuss.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectAll;
import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.view.listener.OnClick;
import com.bhsc.mobile.R;
import com.bhsc.mobile.dataclass.Data_DB_Discuss;
import com.bhsc.mobile.disclose.views.MyListView;
import com.bhsc.mobile.homepage.NewsPresenter;
import com.bhsc.mobile.ThirdParty.ShareActivity;
import com.bhsc.mobile.utils.L;
import com.bhsc.mobile.utils.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynn on 15-10-9.
 */
@InjectLayer(R.layout.activity_discuss)
public class DiscussDetailActivity extends Activity {
    private final String TAG = DiscussDetailActivity.class.getSimpleName();

    class Views{
        @InjectBinder(method = "goBack", listeners = {OnClick.class})
        View activity_discuss_back;
        @InjectBinder(method = "support", listeners = {OnClick.class})
        View activity_discuss_btn_support;
        @InjectBinder(method = "discuss", listeners = {OnClick.class})
        View activity_discuss_btn_discuss;
        @InjectBinder(method = "share", listeners = {OnClick.class})
        View activity_discuss_btn_share;
        @InjectBinder(method = "discuss", listeners = {OnClick.class})
        View activity_discuss_input;
        View activity_discuss_discuss;
        TextView activity_discuss_nickname, activity_discuss_time, activity_discuss_content;
        MyListView activity_discuss_discuss_list;
    }

    @InjectAll
    Views mViews;

    private DiscussAdapter mDiscussAdapter;
    private final List<Data_DB_Discuss> mDiscusses = new ArrayList<>();

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DiscussDetailPresenter.destroy();
    }

    @InjectInit
    private void init(){
        mDiscussAdapter = new DiscussAdapter(this, mDiscusses);
        mViews.activity_discuss_discuss_list.setAdapter(mDiscussAdapter);
        mViews.activity_discuss_discuss_list.setFocusable(false);
        DiscussDetailPresenter.getInstance().getDiscuss();
    }

    private void goBack(){
        finish();
    }

    /**
     * 赞
     */
    private void support(){}

    /**
     * 分享
     */
    private void share(){
        Intent intent = new Intent();
        intent.setClass(this, ShareActivity.class);
        startActivity(intent);
    }

    /**
     * 评论
     */
    private void discuss(){
        showDialag();
    }

    private void onEventMainThread(DiscussEvent event){
        List<Data_DB_Discuss> discusses = event.getDiscusses();
        L.i(TAG, "初始化评论列表:" + discusses.size());
        if(discusses.size() > 0){
            mViews.activity_discuss_discuss.setVisibility(View.VISIBLE);
        }
        mDiscusses.clear();
        mDiscusses.addAll(discusses);
        mDiscussAdapter.notifyDataSetChanged();
    }

    private void showDialag() {
        backgroundAlpha(0.4f);

        View view = LayoutInflater.from(this).inflate(R.layout.pop_input_discuss, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setAnimationStyle(R.style.pop_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        final Button status = (Button) view.findViewById(R.id.pop_input_discuss_status);
        final EditText text = (EditText) view.findViewById(R.id.pop_input_discuss_edit);
        Button publish = (Button) view.findViewById(R.id.pop_input_discuss_publish);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean toUserStatus = (Boolean) status.getTag();
                if (toUserStatus == null || !toUserStatus) {
                    status.setTag(true);
                    status.setBackgroundResource(R.mipmap.btn_status_press);
                } else {
                    status.setTag(false);
                    status.setBackgroundResource(R.mipmap.btn_status_normal);
                }
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discussContent = text.getText().toString();
                if (TextUtils.isEmpty(discussContent)) {
                    return;
                }
                Data_DB_Discuss discuss = new Data_DB_Discuss();
                discuss.setContent(discussContent);
                Boolean toUserStatus = (Boolean) status.getTag();
                if (toUserStatus == null || !toUserStatus) {
                    discuss.setToUserStatus(false);
                } else {
                    discuss.setToUserStatus(true);
                }
                discuss.setCreateTime(Method.getTS());
                NewsPresenter.getInstance(DiscussDetailActivity.this).discussPublish(discuss);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(findViewById(R.id.activity_discuss_container),
                Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
