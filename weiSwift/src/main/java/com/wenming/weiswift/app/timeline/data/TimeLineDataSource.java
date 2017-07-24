package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {

    void requestLatestTimeLine(String accessToken, TimeLinePullToRefreshCallBack callBack);

    void requestLatestTimeLineBySinceId(String accessToken, String sinceId, TimeLinePullToRefreshCallBack callBack);
    
    interface TimeLinePullToRefreshCallBack {
        void onSuccess(List<Status> statusList);

        void onPullToRefreshEmpty();

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }

    interface TimeLineLoadMoreCallBack {
        void onSuccess(List<Status> statusList);

        void onLoadMoreEmpty();

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
