package com.bhsc.net;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/**
 * Created by zhanglei on 16/3/22.
 */
public class MyRetryPolicy implements RetryPolicy {
    @Override
    public int getCurrentTimeout() {
        return 10 * 1000;
    }

    @Override
    public int getCurrentRetryCount() {
        return 3;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {

    }
}
