package com.bhsc.mobile.ThirdParty;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bhsc.mobile.R;
import com.tencent.connect.share.QQShare;

import co.lujun.tpsharelogin.bean.QQShareContent;
import co.lujun.tpsharelogin.bean.WXShareContent;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;

/**
 * Created by zhanglei on 16/5/4.
 */
public class ShareMenu extends PopupWindow implements View.OnClickListener{

    private View mContentView;
    private View mShareWechat, mShareMoments, mShareQQ, mShareBlog, mCancel;
    private Context mContext;
    public ShareMenu(Context context){
        mContext = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.activity_share, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mShareWechat = mContentView.findViewById(R.id.share_wechat);
        mShareMoments = mContentView.findViewById(R.id.share_moments);
        mShareQQ = mContentView.findViewById(R.id.share_qq);
        mShareBlog = mContentView.findViewById(R.id.share_blog);
        mCancel = mContentView.findViewById(R.id.share_cancel);
        mShareWechat.setOnClickListener(this);
        mShareMoments.setOnClickListener(this);
        mShareQQ.setOnClickListener(this);
        mShareBlog.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void show(View parent){
        showAtLocation(parent , Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_wechat:
                shareWXSession();
                break;
            case R.id.share_moments:
                shareWXTimeLine();
                break;
            case R.id.share_qq:
                shareQQ();
                break;
            case R.id.share_blog:
                shareQQBlog();
                break;
            case R.id.share_cancel:
                dismiss();
                break;
        }
    }

    private void shareQQBlog(){
        QQManager qqManager = new QQManager(mContext);
        QQShareContent contentQQ = new QQShareContent();
        contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        contentQQ.setTitle("TPShareLogin Test")
                .setTarget_url("http://lujun.co")
                .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                .setSummary("This is TPShareLogin test, 4 qq!");
        qqManager.share(contentQQ);
    }

    private void shareQQ(){
        QQManager qqManager = new QQManager(mContext);
        QQShareContent contentQQ = new QQShareContent();
        contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        contentQQ.setTitle("TPShareLogin Test")
                .setTarget_url("http://lujun.co")
                .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                .setSummary("This is TPShareLogin test, 4 qq!");
        qqManager.share(contentQQ);
    }

    private void shareWXSession(){
        WXManager wxManager = new WXManager(mContext);
        WXShareContent contentWX = new WXShareContent();
        contentWX.setScene(WXShareContent.WXSession)
                .setWeb_url("http://lujun.co")
                .setTitle("WebTitle")
                .setDescription("Web description, description, description")
                .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                .setType(WXShareContent.share_type.WebPage);
        wxManager.share(contentWX);
    }

    private void shareWXTimeLine(){
        WXManager wxManager = new WXManager(mContext);
        WXShareContent contentWX = new WXShareContent();
        contentWX.setScene(WXShareContent.WXTimeline)
                .setWeb_url("http://lujun.co")
                .setTitle("WebTitle")
                .setDescription("Web description, description, description")
                .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                .setType(WXShareContent.share_type.WebPage);
        wxManager.share(contentWX);
    }
}
