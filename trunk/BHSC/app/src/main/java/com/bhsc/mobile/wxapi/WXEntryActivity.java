package com.bhsc.mobile.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.main.BHApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lynn on 15-10-10.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    private TextView textview;

    public WXEntryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        textview = (TextView) findViewById(R.id.activity_entry_text);
        api = WXAPIFactory.createWXAPI(this, BHApplication.AppID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                showMsg((ShowMessageFromWX.Req) baseReq);
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                showMsg((ShowMessageFromWX.Req) baseReq);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {

    }

    private void showMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // ��֯һ������ʾ����Ϣ����
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);
        textview.setText(msg.toString());
    }
}
