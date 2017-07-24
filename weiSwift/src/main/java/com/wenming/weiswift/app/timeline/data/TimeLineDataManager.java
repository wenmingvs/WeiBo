package com.wenming.weiswift.app.timeline.data;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;
import com.wenming.weiswift.app.timeline.net.TimeLineHttpHepler;
import com.wenming.weiswift.utils.NetUtil;

import java.util.ArrayList;

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
        TimeLineHttpHepler.getTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                StatusList statusList = StatusList.parse(response);
                ArrayList<Status> temp = statusList.statuses;
                if (temp != null && temp.size() > 0) {
                    
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
