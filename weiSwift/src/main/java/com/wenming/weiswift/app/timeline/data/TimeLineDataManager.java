package com.wenming.weiswift.app.timeline.data;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;
import com.wenming.weiswift.app.timeline.constants.Constants;
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
    public void refreshTimeLine(String accessToken, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleRefreshResult(response, callBack);
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
    public void refreshTimeLine(String accessToken, String sinceId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getTimeLine(accessToken, Long.valueOf(sinceId), Constants.TIMELINE_DEFALUT_MAX_ID, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleRefreshResult(response, callBack);
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
    public void loadMoreTimeLine(String accessToken, String maxId, final LoadMoreTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getTimeLine(accessToken, Constants.TIMELINE_DEFALUT_SINCE_ID, Long.valueOf(maxId), mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleLoadMoreResult(response, callBack);
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

    private void handleRefreshResult(String response, RefreshTimeLineCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            callBack.onSuccess(timeLineList);
        } else {
            callBack.onPullToRefreshEmpty();
        }
    }

    private void handleLoadMoreResult(String response, LoadMoreTimeLineCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            //删掉第一条重复的微博
            timeLineList.remove(0);
            callBack.onLoadMoreSuccess(timeLineList);
        } else {
            callBack.onLoadMoreEmpty();
        }
    }

}
