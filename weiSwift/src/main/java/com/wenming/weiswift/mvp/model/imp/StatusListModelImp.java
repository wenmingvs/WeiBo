package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.GroupAPI;
import com.wenming.weiswift.api.StatusesAPI;
import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.list.StatusList;
import com.wenming.weiswift.mvp.model.StatusListModel;
import com.wenming.weiswift.ui.common.NewFeature;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;
import com.wenming.weiswift.utils.SDCardUtil;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class StatusListModelImp implements StatusListModel {
    /**
     * 全局刷新的间隔时间
     */
    private static int REFRESH_FRIENDS_TIMELINE_TASK = 15 * 60 * 1000;
    private ArrayList<Status> mStatusList = new ArrayList<>();
    private Context mContext;
    private OnDataFinishedListener mOnDataFinishedListener;
    private Timer mTimer;
    private TimerTask mTimerTask;
    /**
     * 当前的分组位置
     */
    private long mCurrentPage = Constants.GROUP_TYPE_ALL;
    /**
     * 是否全局刷新
     */
    private boolean mFirstLoad = true;

    /**
     * 是否需要重新刷新整个列表
     */
    private boolean mRefrshFriendsTimeline;


    @Override
    public void friendsTimeline(Context context, OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        setRefrshFriendsTimelineTask();
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        long sinceId = checkout(Constants.GROUP_TYPE_ALL);
        mStatusesAPI.friendsTimeline(sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, pullToRefreshListener);
    }


    @Override
    public void bilateralTimeline(Context context, OnDataFinishedListener onDataFinishedListener) {
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        setRefrshFriendsTimelineTask();
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        long sinceId = checkout(Constants.GROUP_TYPE_FRIENDS_CIRCLE);
        mStatusesAPI.bilateralTimeline(sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, StatusesAPI.FEATURE_ORIGINAL, false, pullToRefreshListener);
    }

    @Override
    public void timeline(long newGroupId, Context context, OnDataFinishedListener onDataFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        setRefrshFriendsTimelineTask();
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        long sinceId = checkout(newGroupId);
        groupAPI.timeline(newGroupId, sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, GroupAPI.FEATURE_ALL, pullToRefreshListener);
    }


    /**
     * 获取我关注的人的下一页微博
     *
     * @param context
     * @param onDataFinishedListener
     */
    @Override
    public void friendsTimelineNextPage(Context context, OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mStatusList.get(mStatusList.size() - 1).id;
        mStatusesAPI.friendsTimeline(0, Long.valueOf(maxId), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, 0, false, nextPageListener);
    }


    /**
     * 获取相互关注的人的下一页微博
     *
     * @param context
     * @param onDataFinishedListener
     */
    @Override
    public void bilateralTimelineNextPage(Context context, OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        String maxId = mStatusList.get(mStatusList.size() - 1).id;
        mStatusesAPI.bilateralTimeline(0, Long.valueOf(maxId), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, StatusesAPI.FEATURE_ORIGINAL, false, nextPageListener);
    }


    /**
     * 获取指定分组的下一页微博
     *
     * @param groundId
     * @param context
     * @param onDataFinishedListener
     */
    @Override
    public void timelineNextPage(long groundId, Context context, OnDataFinishedListener onDataFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnDataFinishedListener = onDataFinishedListener;
        setRefrshFriendsTimelineTask();
        String maxId = mStatusList.get(mStatusList.size() - 1).id;
        groupAPI.timeline(groundId, 0, Long.valueOf(maxId), NewFeature.GET_WEIBO_NUMS, 1, false, GroupAPI.FEATURE_ALL, nextPageListener);

    }

    @Override
    public void friendsTimelineCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp == null || temp.size() == 0) {
                onDataFinishedListener.noMoreData();
            } else {
                mStatusList = temp;
                mCurrentPage = Constants.GROUP_TYPE_ALL;
                onDataFinishedListener.onDataFinish(mStatusList);
            }
        }

    }

    @Override
    public void friendsTimelineCacheSave(Context context, String response) {
        if (NewFeature.CACHE_WEIBOLIST) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
        }
    }


    @Override
    public void setRefrshFriendsTimelineTask() {
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mRefrshFriendsTimeline = true;
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 0, REFRESH_FRIENDS_TIMELINE_TASK);
        }
    }

    @Override
    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    /**
     * 用于更新sinceId和maxId的值
     *
     * @param newGroupId
     * @return
     */
    private long checkout(long newGroupId) {
        long sinceId = 0;
        if (mCurrentPage != newGroupId) {
            mFirstLoad = true;
        }
        if (mStatusList.size() > 0 && mCurrentPage == newGroupId && mFirstLoad == false) {
            sinceId = Long.valueOf(mStatusList.get(0).id);
        }
        if (mRefrshFriendsTimeline) {
            sinceId = 0;
        }
        mCurrentPage = newGroupId;
        return sinceId;
    }


    private RequestListener pullToRefreshListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp != null && temp.size() > 0) {
                if (mStatusList != null) {
                    mStatusList.clear();
                }
                friendsTimelineCacheSave(mContext, response);
                mStatusList = temp;
                mOnDataFinishedListener.onDataFinish(mStatusList);
                mFirstLoad = false;
            } else {
                if (mFirstLoad == false) {//局部刷新，get不到数据
                    ToastUtil.showShort(mContext, "没有更新的内容了");
                    mOnDataFinishedListener.noMoreData();
                } else {//全局刷新，get不到数据
                    mOnDataFinishedListener.noDataInFirstLoad();
                }
            }
            mRefrshFriendsTimeline = false;
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnDataFinishedListener.onError(e.getMessage());
        }
    };


    private RequestListener nextPageListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(mStatusList.get(mStatusList.size() - 1).id))) {
                    mOnDataFinishedListener.noMoreData();
                } else if (temp.size() > 1) {
                    temp.remove(0);
                    mStatusList.addAll(temp);
                    mOnDataFinishedListener.onDataFinish(mStatusList);
                }
            } else {
                mOnDataFinishedListener.noMoreData();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showShort(mContext, e.getMessage());
            mOnDataFinishedListener.onError(e.getMessage());
        }
    };
}
