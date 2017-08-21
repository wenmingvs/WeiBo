package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {

    void loadFriendsTimeLineCache(long uid, LoadCacheCallBack callBack);

    void loadGroupTimeLineCache(long uid, long groupId, LoadCacheCallBack callBack);

    /**
     * 请求好友时间线
     *
     * @param accessToken
     * @param callBack
     */
    void refreshFriendsTimeLine(long uid, String accessToken, RefreshTimeLineCallBack callBack);

    /**
     * 请求第一条微博上面的好友时间线
     *
     * @param accessToken
     * @param sinceId
     * @param callBack
     */
    void refreshFriendsTimeLine(long uid, String accessToken, long sinceId, RefreshTimeLineCallBack callBack);

    /**
     * 加载更多好友时间线
     *
     * @param accessToken
     * @param maxId
     * @param callBack
     */
    void loadMoreFriendsTimeLine(String accessToken, long maxId, LoadMoreTimeLineCallBack callBack);

    /**
     * 请求分组时间线
     *
     * @param accessToken
     * @param groupId
     * @param callBack
     */
    void refreshGroupTimeLine(long uid, String accessToken, long groupId, RefreshTimeLineCallBack callBack);

    /**
     * 请求分组下第一条微博上面的时间线
     *
     * @param accessToken
     * @param groupId
     * @param sinceId
     * @param callBack
     */
    void refreshGroupTimeLine(long uid, String accessToken, long groupId, long sinceId, RefreshTimeLineCallBack callBack);

    /**
     * 加载更多分组时间线
     *
     * @param accessToken
     * @param groupId
     * @param maxId
     * @param callBack
     */
    void loadMoreGroupTimeLine(String accessToken, long groupId, long maxId, LoadMoreTimeLineCallBack callBack);

    /**
     * 将一个或多个短链接还原成原始的长链接
     *
     * @param accessToken
     * @param urlList
     */
    void parseShortUrl(String accessToken, List<String> urlList, ParseShortUrlCallBack callBack);

    interface RequestVideoImgCallBack {
        void onSuccess(String videoUrl);

        void onFail();
    }

    interface LoadCacheCallBack {
        void onComplete(List<Status> data);

        void onEmpty();
    }

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

    interface ParseShortUrlCallBack {
        void onSuccess();

        void onFail();

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
