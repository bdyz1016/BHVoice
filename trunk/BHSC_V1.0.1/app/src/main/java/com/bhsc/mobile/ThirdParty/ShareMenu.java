package com.bhsc.mobile.ThirdParty;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.bhsc.mobile.R;
import com.bhsc.mobile.utils.L;
import com.tencent.connect.share.QQShare;

import co.lujun.tpsharelogin.bean.QQShareContent;
import co.lujun.tpsharelogin.bean.WXShareContent;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;

/**
 * Created by zhanglei on 16/5/4.
 */
public class ShareMenu extends PopupWindow implements View.OnClickListener{
    private final String TAG = ShareMenu.class.getSimpleName();

    private View mContentView;
    private View mShareWechat, mShareMoments, mShareQQ, mShareBlog, mCancel;
    private Context mContext;
    private ShareContent mShareContent;
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

    public void show(View parent, ShareContent shareContent){
        mShareContent = shareContent;
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
        }
        dismiss();
    }

    private void shareQQBlog(){
        L.i(TAG, "shareQQBlog");
        QQManager qqManager = new QQManager(mContext);
        QQShareContent contentQQ = new QQShareContent();
        contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        contentQQ.setTitle(mShareContent.getTitle())
                .setTarget_url(mShareContent.getWebUrl())
                .setImage_url(mShareContent.getImageUrl())
                .setSummary(mShareContent.getDescription());
        qqManager.share(contentQQ);
    }

    private void shareQQ(){
        L.i(TAG, "shareQQ");
        QQManager qqManager = new QQManager(mContext);
        QQShareContent contentQQ = new QQShareContent();
        contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        contentQQ.setTitle(mShareContent.getTitle())
                .setTarget_url(mShareContent.getWebUrl())
                .setImage_url(mShareContent.getImageUrl())
                .setSummary(mShareContent.getDescription());
        qqManager.share(contentQQ);
    }

    private void shareWXSession(){
        L.i(TAG, "shareWXSession");
        WXManager wxManager = new WXManager(mContext);
        WXShareContent contentWX = new WXShareContent();
        contentWX.setScene(WXShareContent.WXSession)
                .setWeb_url(mShareContent.getWebUrl())
                .setTitle(mShareContent.getTitle())
                .setDescription(mShareContent.getDescription())
                .setImage_url(mShareContent.getImageUrl())
                .setType(WXShareContent.share_type.WebPage);
        wxManager.share(contentWX);
    }

    private void shareWXTimeLine(){
        L.i(TAG, "shareWXTimeLine");
        WXManager wxManager = new WXManager(mContext);
        WXShareContent contentWX = new WXShareContent();
        contentWX.setScene(WXShareContent.WXTimeline)
                .setWeb_url(mShareContent.getWebUrl())
                .setTitle(mShareContent.getTitle())
                .setDescription(mShareContent.getDescription())
                .setImage_url(mShareContent.getImageUrl())
                .setType(WXShareContent.share_type.WebPage);
        wxManager.share(contentWX);
    }

    public static class ShareContent{
        private String title;
        private String description;
        private String imageUrl;
        private String webUrl;
        public ShareContent(@NonNull String title, @NonNull String description, @NonNull String imageUrl, @NonNull String webUrl){
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.webUrl = webUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }
    }
}
