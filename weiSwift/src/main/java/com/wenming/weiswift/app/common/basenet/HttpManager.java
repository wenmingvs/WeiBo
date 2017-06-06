package com.wenming.weiswift.app.common.basenet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HttpManager {
    private final static String TAG = HttpManager.class.getName();
    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        VolleyController.instance().init(context);
    }

    public static void httpStringPostRequest(String url, final Map<String, String> params, Object requestTag,
                                             Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }

    public static void httpStringGetRequest(String url, final Map<String, String> params, Object requestTag,
                                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }
}
