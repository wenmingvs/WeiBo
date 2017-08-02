package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {

    void refreshTimeLine(String accessToken, RefreshTimeLineCallBack callBack);

    void refreshTimeLine(String accessToken, String sinceId, RefreshTimeLineCallBack callBack);

    void loadMoreTimeLine(String accessToken, String maxId, LoadMoreTimeLineCallBack callBack);

    interface RefreshTimeLineCallBack {
        void onSuccess(List<Status> statusList);

        void onPullToRefreshEmpty();

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }

    interface LoadMoreTimeLineCallBack {
        void onLoadMoreSuccess(List<Status> statusList);

        void onLoadMoreEmpty();

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
