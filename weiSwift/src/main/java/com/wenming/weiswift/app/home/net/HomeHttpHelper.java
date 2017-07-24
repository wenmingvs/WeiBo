package com.wenming.weiswift.app.home.net;

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
}
