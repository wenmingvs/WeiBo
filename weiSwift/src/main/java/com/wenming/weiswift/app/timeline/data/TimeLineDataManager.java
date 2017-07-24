package com.wenming.weiswift.app.timeline.data;

import android.content.Context;
import android.text.TextUtils;

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
    public void requestLatestTimeLine(String accessToken, final TimeLinePullToRefreshCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pullToRefreshSuccess(response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    @Override
    public void requestLatestTimeLineBySinceId(String accessToken, String sinceId, final TimeLinePullToRefreshCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getTimeLine(accessToken, Long.valueOf(sinceId), mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pullToRefreshSuccess(response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (TextUtils.isEmpty(error.getCause().getMessage())) {
                    callBack.onTimeOut();
                }
            }
        });
    }

    private void pullToRefreshSuccess(String response, TimeLinePullToRefreshCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            callBack.onSuccess(timeLineList);
        } else {
            callBack.onPullToRefreshEmpty();
        }
    }

}
