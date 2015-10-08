package com.dp2px;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lynn on 15-9-29.
 */
public class ColorFragment extends Fragment {
    private View mContentView;

    private EditText Edit_Red, Edit_Green, Edit_Blue;
    private TextView Tv_Result;
    private Button Btn_Convert;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_color, container, false);
        initWidget();
        return mContentView;
    }

    private void initWidget(){
        Edit_Red = (EditText) mContentView.findViewById(R.id.red);
        Edit_Green = (EditText) mContentView.findViewById(R.id.green);
        Edit_Blue = (EditText) mContentView.findViewById(R.id.blue);
        Tv_Result = (TextView) mContentView.findViewById(R.id.result);
        Btn_Convert = (Button) mContentView.findViewById(R.id.convert);

        Btn_Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String red = Edit_Red.getText().toString();
                String green = Edit_Green.getText().toString();
                String blue = Edit_Blue.getText().toString();
                if(TextUtils.isEmpty(red) || TextUtils.isEmpty(green) || TextUtils.isEmpty(blue)){
                    return;
                }
                String result;
                result = Integer.toHexString(Integer.parseInt(red)) + "" + Integer.toHexString(Integer.parseInt(green)) + "" + Integer.toHexString(Integer.parseInt(blue));
                Tv_Result.setText(result);
            }
        });
    }
}
