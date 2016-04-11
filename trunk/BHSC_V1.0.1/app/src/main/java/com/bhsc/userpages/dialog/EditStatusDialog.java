package com.bhsc.userpages.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bhsc.mobile.R;

/**
 * Created by lynn on 10/27/15.
 */
public class EditStatusDialog extends Dialog {
    public interface OnPositiveButtonClickListener{
        void onConfirm(String status);
    }

    public EditStatusDialog(Context context) {
        super(context, R.style.DefaultDialogStyle);
    }

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener = new OnPositiveButtonClickListener() {
        @Override
        public void onConfirm(String nickname) {

        }
    };

    private EditText Edit_Status;
    private Button Btn_Cancel, Btn_Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_status);
        Edit_Status = (EditText) findViewById(R.id.dialog_edit_status_text);
        Btn_Cancel = (Button) findViewById(R.id.dialog_edit_status_cancel);
        Btn_Confirm = (Button) findViewById(R.id.dialog_edit_status_confirm);
        Btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = Edit_Status.getText().toString();
                if(!TextUtils.isEmpty(status)){
                    mOnPositiveButtonClickListener.onConfirm(status);
                    dismiss();
                }
            }
        });
        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener listener){
        this.mOnPositiveButtonClickListener = listener;
    }
}
