package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {

    void refreshDefaultTimeLine(String accessToken, RefreshTimeLineCallBack callBack);

    void refreshDefaultTimeLine(String accessToken, String sinceId, RefreshTimeLineCallBack callBack);

    void loadMoreDefaultTimeLine(String accessToken, String maxId, LoadMoreTimeLineCallBack callBack);

    void refreshGroupTimeLine(String accessToken, long groupId, RefreshTimeLineCallBack callBack);

    void refreshGroupTimeLine(String accessToken, long groupId, String sinceId, RefreshTimeLineCallBack callBack);

    void loadMoreGroupTimeLine(String accessToken, long groupId, String maxId, LoadMoreTimeLineCallBack callBack);

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
