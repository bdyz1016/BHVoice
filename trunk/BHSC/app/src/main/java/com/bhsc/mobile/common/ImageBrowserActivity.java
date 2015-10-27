
package com.bhsc.mobile.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bhsc.mobile.R;
import com.bhsc.mobile.main.BHApplication;
import com.bhsc.mobile.media.ImageUtil;

public class ImageBrowserActivity extends Activity {
    public static final String INTENT_PATH = "path";

    private String mPath = "";
    private ImageView Iv_Image;
    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_browser);
        mPath = getIntent().getStringExtra(INTENT_PATH);

        Iv_Image = (ImageView) findViewById(R.id.activity_image_browser);

        Iv_Image.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageBrowserActivity.this.finish();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBitmap = ImageUtil.getInstance().decodeSampledBitmapFromSource(mPath, BHApplication.screenWidth, BHApplication.screenHeight);
        Iv_Image.setImageBitmap(mBitmap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBitmap.recycle();
        mBitmap = null;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
