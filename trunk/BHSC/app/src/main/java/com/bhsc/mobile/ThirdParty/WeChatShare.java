package com.bhsc.mobile.ThirdParty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bhsc.mobile.utils.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created by lynn on 15-10-10.
 */
public class WeChatShare {

    public static final int THUMB_SIZE = 150;

    /**
     * 分享文字到朋友圈
     * @param api
     * @param content 分享内容
     */
    public static void shareTextToTimeline(IWXAPI api, String content){
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);
    }

    /**
     * 分享文字给好友
     * @param api
     * @param content 分享内容
     */
    public static void shareTextToSession(IWXAPI api, String content){
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        api.sendReq(req);
    }

    /**
     * 分享图片到朋友圈
     * @param api
     * @param bmp 发送的图片
     * @param dstWidth
     * @param dstHeight
     */
    public static void shareImgToTimeline(IWXAPI api, Bitmap bmp, int dstWidth, int dstHeight){
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // ��������ͼ

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享图片给好友
     * @param api
     * @param bmp 发送的图片
     * @param dstWidth
     * @param dstHeight
     */
    public static void shareImgToSession(IWXAPI api, Bitmap bmp, int dstWidth, int dstHeight){
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // ��������ͼ

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 分享图片到朋友圈
     * @param api
     * @param path 发送的图片路径
     * @param dstWidth
     * @param dstHeight
     */
    public static void shareImgToTimeline(IWXAPI api, String path, int dstWidth, int dstHeight){
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 分享图片给好友
     * @param api
     * @param path 发送的图片路径
     * @param dstWidth
     * @param dstHeight
     */
    public static void shareImgToSession(IWXAPI api, String path, int dstWidth, int dstHeight){
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
