package com.wenming.weiswift.app.home.data;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.user.UserManager;
import com.wenming.weiswift.app.home.data.entity.Group;
import com.wenming.weiswift.app.home.data.entity.GroupList;
import com.wenming.weiswift.app.home.net.HomeHttpHelper;
import com.wenming.weiswift.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HomeDataManager implements HomeDataSource {
    private Context mContext;
    private Object mRequestTag = new Object();

    public HomeDataManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void requestGroups(String accessToken, final GroupCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        HomeHttpHelper.getGroups(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.has("error")) {
                        UserManager.getInstance().setUserGroups(response);
                        List<Group> groups = GroupList.parse(response).lists;
                        callBack.onSuccess(groups);
                    } else {
                        String errorString = jsonObject.optString("error");
                        callBack.onFail(errorString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    callBack.onTimeOut();
                }
                String body = null;
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                callBack.onFail(body);
            }
        });
    }
}
