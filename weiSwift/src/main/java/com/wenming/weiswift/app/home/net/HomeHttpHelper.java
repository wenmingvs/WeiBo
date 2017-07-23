package com.wenming.weiswift.app.home.net;

import android.text.TextUtils;

import com.android.volley.Response;
import com.wenming.weiswift.app.common.basenet.HttpManager;
import com.wenming.weiswift.app.common.constants.APIConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HomeHttpHelper {

    public static void getGroups(String token, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        HttpManager.getInstance().httpStringGetRequest(APIConstants.GROUPS, params, requesetTag, listener, errorListener);
    }

    public static void getTimeLine(String token, Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        HttpManager.getInstance().httpStringGetRequest(APIConstants.FRIENDS_TIMELINE, params, requesetTag, listener, errorListener);
    }

    public static void getTimeLine(String token, long sinceId, long maxId, String count, String page, String baseApp, String feature, String trimUser,
                                   Object requesetTag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(token)){
            params.put("access_token", token);
        }else {

        }
        params.put("since_id", String.valueOf(sinceId));
        params.put("max_id", String.valueOf(maxId));
        params.put("count", String.valueOf(count));


        HttpManager.getInstance().httpStringGetRequest(APIConstants.FRIENDS_TIMELINE, params, requesetTag, listener, errorListener);
    }


}
