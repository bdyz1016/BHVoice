package com.bhsc.news;

import android.app.Activity;
import android.os.Bundle;
import com.bhsc.mobile.R;

/**
 * Created by lynn on 15-10-8.
 */
public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    private void init(){

    }

    private void cancel(){
        finish();
    }
}
