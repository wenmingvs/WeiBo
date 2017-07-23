package com.wenming.weiswift.app.timeline.data;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.home.net.HomeHttpHelper;
import com.wenming.weiswift.utils.NetUtil;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class TimeLineDataManager implements TimeLineDataSource {
    private Context mContext;
    private Object mRequestTag = new Object();


    public TimeLineDataManager(Context context) {
        this.mContext = context;
    }

    @Override
    public void requestTimeLine(String accessToken, TimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        HomeHttpHelper.getTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }
}
