package com.bhsc.mobile.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bhsc.mobile.R;

import org.w3c.dom.Text;

/**
 * Created by lynn on 10/27/15.
 */
public class EditNicknameDialog extends Dialog {

    public interface OnPositiveButtonClickListener{
        void onConfirm(String nickname);
    }

    public EditNicknameDialog(Context context) {
        super(context, R.style.DefaultDialogStyle);
    }

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener = new OnPositiveButtonClickListener() {
        @Override
        public void onConfirm(String nickname) {

        }
    };

    private EditText Edit_Nickname;
    private Button Btn_Cancel, Btn_Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_nickname);
        Edit_Nickname = (EditText) findViewById(R.id.dialog_edit_nickname_text);
        Btn_Cancel = (Button) findViewById(R.id.dialog_edit_nickname_cancel);
        Btn_Confirm = (Button) findViewById(R.id.dialog_edit_nickname_confirm);
        Btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = Edit_Nickname.getText().toString();
                if(!TextUtils.isEmpty(nickname)){
                    mOnPositiveButtonClickListener.onConfirm(nickname);
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
