package com.dp2px;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.agimind.widget.SlideHolder;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private View mDp2px, mColor;
    private SlideHolder mSlideHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        switchDp2px();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_dp2px:
                switchDp2px();
                break;
            case R.id.btn_color:
                switchColor();
                break;
            default:
                break;
        }
        mSlideHolder.toggle();
    }

    private void initWidget(){
        mSlideHolder = (SlideHolder)findViewById(R.id.slideHolder);
        mDp2px = findViewById(R.id.btn_dp2px);
        mColor = findViewById(R.id.btn_color);
        mDp2px.setOnClickListener(this);
        mColor.setOnClickListener(this);
    }

    private void switchDp2px(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new Dp2pxFragment()).commit();
    }

    private void switchColor(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ColorFragment()).commit();
    }
}
