//package com.bhsc.ThirdParty;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.widget.Button;
//
//import com.android.pc.ioc.inject.InjectAll;
//import com.android.pc.ioc.inject.InjectBinder;
//import com.android.pc.ioc.inject.InjectInit;
//import com.android.pc.ioc.inject.InjectLayer;
//import com.android.pc.ioc.view.listener.OnClick;
//import com.bhsc.mobile.R;
//import com.tencent.connect.share.QQShare;
//import com.tencent.connect.share.QzoneShare;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;
//
//import java.util.ArrayList;
//
///**
// * Created by lynn on 15-10-10.
// */
//@InjectLayer(R.layout.activity_share)
//public class ShareActivity extends Activity implements IUiListener {
//
//    class Views {
//        @InjectBinder(method = "shareWechat", listeners = {OnClick.class})
//        Button activity_share_wechat;
//        @InjectBinder(method = "shareMoments", listeners = {OnClick.class})
//        Button activity_share_moments;
//        @InjectBinder(method = "shareQQ", listeners = {OnClick.class})
//        Button activity_share_qq;
//        @InjectBinder(method = "shareBlog", listeners = {OnClick.class})
//        Button activity_share_blog;
//        @InjectBinder(method = "cancel", listeners = {OnClick.class})
//        Button activity_share_cancel;
//    }
//
//    @InjectAll
//    Views mViews;
//
//    private IWXAPI api;
//
//    private TencentQQ mTencentQQ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @InjectInit
//    private void init() {
//        api = WXAPIFactory.createWXAPI(this, WeChatShare.AppID, true);
//        mTencentQQ = TencentQQ.getInstance(this);
//    }
//
//    private void cancel() {
//        finish();
//    }
//
//    /**
//     * 分享到微信
//     */
//    private void shareWechat() {
//        WeChatShare.shareTextToTimeline(api, "simple test");
//    }
//
//    /**
//     * 分享到朋友圈
//     */
//    private void shareMoments() {
//        WeChatShare.shareTextToSession(api, "simple test");
//    }
//
//    /**
//     * 分享到QQ
//     */
//    private void shareQQ() {
//        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getResources().getString(R.string.app_name));
//        mTencentQQ.shareToQQ(params, ShareActivity.this);
//    }
//
//    /**
//     * 分享到QQ空间
//     */
//    private void shareBlog() {
//        ArrayList<String> images = new ArrayList<String>();
//        images.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        final Bundle params = new Bundle();
//        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_NO_TYPE);
//        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "test");//必填
//        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "这是一个测试");//选填
//        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");//必填
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
//        mTencentQQ.shareToQQ(params, ShareActivity.this);
//    }
//
//    @Override
//    public void onComplete(Object o) {
//
//    }
//
//    @Override
//    public void onError(UiError uiError) {
//
//    }
//
//    @Override
//    public void onCancel() {
//
//    }
//}
