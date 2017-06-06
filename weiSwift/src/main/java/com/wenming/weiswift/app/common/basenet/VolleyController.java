package com.wenming.weiswift.app.common.basenet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class VolleyController {
    private static final String TAG = VolleyController.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;
    private static volatile VolleyController mInstance;

    private VolleyController() {
    }

    public static VolleyController instance() {
        if (mInstance == null) {
            synchronized (VolleyController.class) {
                if (mInstance == null) {
                    mInstance = new VolleyController();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, Object tag) {
        req.setTag(tag == null ? TAG : tag);
        VolleyLog.d("Adding request to volley queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
