package com.bhsc.mobile.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bhsc.mobile.R;

/**
 * Created by lynn on 10/15/15.
 */
public class DefaultDialog extends Dialog implements View.OnClickListener{

    public interface OnButtonClickListener{
        void onClick();
    }

    private OnButtonClickListener mPositiveClicked;
    private OnButtonClickListener mNegativeClicked;

    private DialogControler mDialogControler;

    public DefaultDialog(Context context) {
//        this(context, false);
        super(context, R.style.DefaultDialogStyle);
        mDialogControler = new DialogControler();
    }

    private Button Btn_Positive, Btn_Negative;
    private View View_Divider;
    private TextView Tv_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        Btn_Positive = (Button) findViewById(R.id.dialog_alert_positive);
        Btn_Negative = (Button) findViewById(R.id.dialog_alert_negative);
        Btn_Positive.setOnClickListener(this);
        Btn_Negative.setOnClickListener(this);

        Tv_Message = (TextView) findViewById(R.id.dialog_alert_message);

        View_Divider = findViewById(R.id.dialog_alert_divider);

        if(!mDialogControler.isPositiveButton()) {
            Btn_Positive.setVisibility(View.GONE);
        }
        if(!mDialogControler.isNegativeButton()) {
            Btn_Negative.setVisibility(View.GONE);
        }
        if(mDialogControler.isNegativeButton() && mDialogControler.isPositiveButton()){
            View_Divider.setVisibility(View.VISIBLE);
        } else {
            View_Divider.setVisibility(View.GONE);
        }

        Tv_Message.setText(mDialogControler.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_alert_positive:
                if(mPositiveClicked != null) {
                    mPositiveClicked.onClick();
                }
                break;
            case R.id.dialog_alert_negative:
                if(mNegativeClicked != null) {
                    mNegativeClicked.onClick();
                }
                break;
            default:
                break;
        }
    }

    public void setOnPositiveClickListener(OnButtonClickListener listener){
        this.mPositiveClicked = listener;
        mDialogControler.setPositiveButton(true);
    }

    public void setOnNegativeClickListener(OnButtonClickListener listener){
        this.mNegativeClicked = listener;
        mDialogControler.setNegativeButton(true);
    }

    public void setMessage(String msg){
        if(msg == null){
            return;
        }
        mDialogControler.setMessage(msg);
    }

    @Override
    public void show() {
        super.show();
    }
}
