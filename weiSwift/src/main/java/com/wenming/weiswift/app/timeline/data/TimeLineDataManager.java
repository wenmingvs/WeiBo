package com.wenming.weiswift.app.timeline.data;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;
import com.wenming.weiswift.app.timeline.cache.TimeLineCacheConfig;
import com.wenming.weiswift.app.timeline.constants.Constants;
import com.wenming.weiswift.app.timeline.net.TimeLineHttpHepler;
import com.wenming.weiswift.app.utils.TextSaveUtils;
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
    public void loadFriendsTimeLineCache(long uid, final LoadCacheCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(TimeLineCacheConfig.getFriendsTimeLine(), TimeLineCacheConfig.FILE_FRIENDS_TIMELINE);
                if (!TextUtils.isEmpty(json)) {
                    callBack.onComplete(StatusList.parse(json).statuses);
                } else {
                    callBack.onEmpty();
                }
            }
        });
    }

    @Override
    public void loadGroupTimeLineCache(long uid, final long groupId, final LoadCacheCallBack callBack) {
        ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
            @Override
            public void onRun() {
                String json = TextSaveUtils.read(TimeLineCacheConfig.getGroupsTimeLineDir(),
                        TimeLineCacheConfig.FILE_GROUPS_TIMELINE_PRRFIX + String.valueOf(groupId) + TimeLineCacheConfig.FILE_GROUPS_TIMELINE_SUFFIX);
                if (!TextUtils.isEmpty(json)) {
                    callBack.onComplete(StatusList.parse(json).statuses);
                } else {
                    callBack.onEmpty();
                }
            }
        });
    }

    @Override
    public void refreshFriendsTimeLine(String accessToken, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getDefaultTimeLine(accessToken, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                handleFriendTimeLineRefreshResult(response, callBack);
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
    public void refreshFriendsTimeLine(String accessToken, String sinceId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getDefaultTimeLine(accessToken, Long.valueOf(sinceId), Constants.TIMELINE_DEFALUT_MAX_ID, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                handleFriendTimeLineRefreshResult(response, callBack);
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
    public void loadMoreFriendsTimeLine(String accessToken, String maxId, final LoadMoreTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getDefaultTimeLine(accessToken, Constants.TIMELINE_DEFALUT_SINCE_ID, Long.valueOf(maxId), mRequestTag, new Response.Listener<String>() {
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

    @Override
    public void refreshGroupTimeLine(String accessToken, final long groupId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                handleGroupsTimeLineRefreshResult(response, groupId, callBack);
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
    public void refreshGroupTimeLine(String accessToken, final long groupId, String sinceId, final RefreshTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, mRequestTag, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                handleGroupsTimeLineRefreshResult(response, groupId, callBack);
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
    public void loadMoreGroupTimeLine(String accessToken, long groupId, String maxId, final LoadMoreTimeLineCallBack callBack) {
        if (!NetUtil.isConnected(mContext)) {
            callBack.onNetWorkNotConnected();
            return;
        }
        TimeLineHttpHepler.getGroupsTimeLine(accessToken, groupId, Constants.TIMELINE_DEFALUT_SINCE_ID, Long.valueOf(maxId), mRequestTag, new Response.Listener<String>() {
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

    private void handleFriendTimeLineRefreshResult(final String response, RefreshTimeLineCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                @Override
                public void onRun() {
                    TextSaveUtils.write(TimeLineCacheConfig.getFriendsTimeLine(), TimeLineCacheConfig.FILE_FRIENDS_TIMELINE, response);
                }
            });
            callBack.onSuccess(timeLineList);
        } else {
            callBack.onPullToRefreshEmpty();
        }
    }

    private void handleGroupsTimeLineRefreshResult(final String response, final long groupId, RefreshTimeLineCallBack callBack) {
        StatusList statusList = StatusList.parse(response);
        ArrayList<Status> timeLineList = statusList.statuses;
        if (timeLineList != null && timeLineList.size() > 0) {
            ThreadHelper.instance().runOnWorkThread(new ThreadHelper.Task() {
                @Override
                public void onRun() {
                    TextSaveUtils.write(TimeLineCacheConfig.getGroupsTimeLineDir(),
                            TimeLineCacheConfig.FILE_GROUPS_TIMELINE_PRRFIX + String.valueOf(groupId) + TimeLineCacheConfig.FILE_GROUPS_TIMELINE_SUFFIX, response);
                }
            });
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
