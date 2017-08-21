package com.wenming.weiswift.app.common.basenet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HttpManager {
    private final static String TAG = "HttpManager";
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

    public static void httpStringPostRequest(String url, final Map<String, String> params, Object requestTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getFinalUrl(url, params), listener, errorListener);
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }

    public static void httpStringGetRequest(String url, final Map<String, String> params, Object requestTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getFinalUrl(url, params), listener, errorListener);
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }

    public static void httpStringGetRequest(String url, final Map<String, String> params, String paramListName, List urlList, Object requestTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getFinalUrl(url, params, paramListName, urlList), listener, errorListener);
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }

    public static void httpStringGetRequest(String url, Object requestTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(url, listener, errorListener);
        VolleyController.instance().addToRequestQueue(stringRequest, requestTag);
    }

    /**
     * 拼接Get操作的网络请求
     *
     * @param url    原始地址
     * @param params 参数
     * @return 返回最终的url
     */
    private static String getFinalUrl(String url, Map<String, String> params, String paramsListName, List<String> paramsList) {
        // 添加url参数
        if (params != null) {
            //拼接hashMap中的数据
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            //拼接paramsList中的数据
            for (int i = 0; i < paramsList.size(); i++) {
                sb.append("&url_short=" + paramsList.get(i));
            }
            if (sb != null) {
                url += sb.toString();
            } else {
                url += null;
            }
        }
        return url;
    }

    /**
     * 拼接Get操作的网络请求
     *
     * @param url    原始地址
     * @param params 参数
     * @return 返回最终的url
     */
    private static String getFinalUrl(String url, Map<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            if (sb != null) {
                url += sb.toString();
            } else {
                url += null;
            }
        }
        return url;
    }
}
