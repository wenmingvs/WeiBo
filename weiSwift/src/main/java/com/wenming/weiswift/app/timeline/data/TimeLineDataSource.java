package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {

    /**
     * 请求好友时间线
     *
     * @param accessToken
     * @param callBack
     */
    void refreshDefaultTimeLine(String accessToken, RefreshTimeLineCallBack callBack);

    /**
     * 请求第一条微博上面的好友时间线
     *
     * @param accessToken
     * @param sinceId
     * @param callBack
     */
    void refreshDefaultTimeLine(String accessToken, String sinceId, RefreshTimeLineCallBack callBack);

    /**
     * 加载更多好友时间线
     *
     * @param accessToken
     * @param maxId
     * @param callBack
     */
    void loadMoreDefaultTimeLine(String accessToken, String maxId, LoadMoreTimeLineCallBack callBack);

    /**
     * 请求分组时间线
     * @param accessToken
     * @param groupId
     * @param callBack
     */
    void refreshGroupTimeLine(String accessToken, long groupId, RefreshTimeLineCallBack callBack);

    /**
     * 请求分组下第一条微博上面的时间线
     * @param accessToken
     * @param groupId
     * @param sinceId
     * @param callBack
     */
    void refreshGroupTimeLine(String accessToken, long groupId, String sinceId, RefreshTimeLineCallBack callBack);

    /**
     * 加载更多分组时间线
     * @param accessToken
     * @param groupId
     * @param maxId
     * @param callBack
     */
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
