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
    private static final int REFRESH_FRIENDS_TIMELINE_TASK = 15 * 60 * 1000;
    private ArrayList<Status> mFriendsTimeline = new ArrayList<>();
    private ArrayList<Status> mGroupList = new ArrayList<>();

    private TimerTask mRefrshFriendsTimelineTask;
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
    public void friendsTimeline(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mCurrentPage != Constants.GROUP_TYPE_ALL) {
            mFirstLoad = true;
        }
        if (mFriendsTimeline.size() > 0 && mCurrentPage == Constants.GROUP_TYPE_ALL && mFirstLoad == false) {
            sinceId = Long.valueOf(mFriendsTimeline.get(0).id);
        }
        if (mRefrshFriendsTimeline) {
            sinceId = 0;
        }
        mCurrentPage = Constants.GROUP_TYPE_ALL;
        mStatusesAPI.friendsTimeline(sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mFriendsTimeline != null) {
                        mFriendsTimeline.clear();
                    }
                    friendsTimelineCacheSave(context, response);
                    mFriendsTimeline = temp;
                    onDataFinishedListener.onDataFinish(mFriendsTimeline);
                    mFirstLoad = false;
                } else {
                    if (mFirstLoad == false) {//局部刷新，get不到数据
                        //ToastUtil.showShort(context, "没有更新的内容了");
                        ToastUtil.showShort(context, "局部刷新，get不到数据");
                        onDataFinishedListener.noMoreData();
                    } else {//全局刷新，get不到数据
                        ToastUtil.showShort(context, "全局刷新，get不到数据");
                        onDataFinishedListener.noDataInFirstLoad();
                    }

                }
                mRefrshFriendsTimeline = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void friendsTimelineNextPage(final Context context, final OnDataFinishedListener onRequestFinishedListener) {
        setRefrshFriendsTimelineTask();
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        final String maxId = mFriendsTimeline.get(mFriendsTimeline.size() - 1).id;
        mStatusesAPI.friendsTimeline(0, Long.valueOf(maxId), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(maxId))) {
                        onRequestFinishedListener.noMoreData();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mFriendsTimeline.addAll(temp);
                        onRequestFinishedListener.onDataFinish(mFriendsTimeline);
                    }
                } else {
                    onRequestFinishedListener.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onRequestFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void friendsTimelineCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp == null || temp.size() == 0) {
                onDataFinishedListener.noMoreData();
            } else {
                mFriendsTimeline = temp;
                mCurrentPage = Constants.GROUP_TYPE_ALL;
                onDataFinishedListener.onDataFinish(mFriendsTimeline);
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
    public void bilateralTimeline(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mCurrentPage != Constants.GROUP_TYPE_FRIENDS_CIRCLE) {
            mFirstLoad = true;
        }
        if (mFriendsTimeline.size() > 0 && mCurrentPage == Constants.GROUP_TYPE_FRIENDS_CIRCLE && mFirstLoad == false) {
            sinceId = Long.valueOf(mFriendsTimeline.get(0).id);
        }
        if (mRefrshFriendsTimeline) {
            sinceId = 0;
        }
        mCurrentPage = Constants.GROUP_TYPE_FRIENDS_CIRCLE;
        mStatusesAPI.bilateralTimeline(sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, StatusesAPI.FEATURE_ORIGINAL, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mFriendsTimeline != null) {
                        mFriendsTimeline.clear();
                    }
                    friendsTimelineCacheSave(context, response);
                    mFriendsTimeline = temp;
                    mFirstLoad = false;
                    onDataFinishedListener.onDataFinish(mFriendsTimeline);
                } else {
                    if (mFirstLoad == false) {//局部刷新，get不到数据
                        ToastUtil.showShort(context, "没有更新的内容了");
                        onDataFinishedListener.noMoreData();
                    } else {//全局刷新，get不到数据
                        onDataFinishedListener.noDataInFirstLoad();
                    }
                }
                mRefrshFriendsTimeline = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void bilateralTimelineNextPage(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        final String maxId = mFriendsTimeline.get(mFriendsTimeline.size() - 1).id;
        mStatusesAPI.bilateralTimeline(0, Long.valueOf(maxId), NewFeature.LOADMORE_WEIBO_ITEM, 1, false, StatusesAPI.FEATURE_ORIGINAL, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(maxId))) {
                        onDataFinishedListener.noMoreData();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mFriendsTimeline.addAll(temp);
                        onDataFinishedListener.onDataFinish(mFriendsTimeline);
                    }
                } else {
                    onDataFinishedListener.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }


    @Override
    public void timeline(long newGroupId, final Context context, final OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mCurrentPage != newGroupId) {
            mFirstLoad = true;
        }
        if (mGroupList.size() > 0 && mCurrentPage == newGroupId && mFirstLoad == false) {
            sinceId = Long.valueOf(mGroupList.get(0).id);
        }
        if (mRefrshFriendsTimeline) {
            sinceId = 0;
        }
        mCurrentPage = newGroupId;
        groupAPI.timeline(newGroupId, sinceId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, GroupAPI.FEATURE_ALL, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mGroupList != null) {
                        mGroupList.clear();
                    }
                    mGroupList = temp;
                    mFirstLoad = false;
                    onDataFinishedListener.onDataFinish(mGroupList);
                } else {
                    if (mFirstLoad == false) {//局部刷新，get不到数据
                        ToastUtil.showShort(context, "没有更新的内容了");
                        onDataFinishedListener.noMoreData();
                    } else {//全局刷新，get不到数据
                        onDataFinishedListener.noDataInFirstLoad();
                    }
                }
                mRefrshFriendsTimeline = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void timelineNextPage(long groundId, final Context context, final OnDataFinishedListener onDataFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        setRefrshFriendsTimelineTask();
        final String maxId = mGroupList.get(mGroupList.size() - 1).id;
        groupAPI.timeline(groundId, 0, Long.valueOf(maxId), NewFeature.GET_WEIBO_NUMS, 1, false, GroupAPI.FEATURE_ALL, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(maxId))) {
                        onDataFinishedListener.noMoreData();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mGroupList.addAll(temp);
                        onDataFinishedListener.onDataFinish(mGroupList);
                    }
                } else {
                    onDataFinishedListener.noMoreData();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
                onDataFinishedListener.onError(e.getMessage());
            }
        });

    }

    public void setRefrshFriendsTimelineTask() {
        if (mRefrshFriendsTimelineTask == null) {
            mRefrshFriendsTimelineTask = new TimerTask() {
                @Override
                public void run() {
                    mRefrshFriendsTimeline = true;
                }
            };
            new Timer().schedule(mRefrshFriendsTimelineTask, 0, REFRESH_FRIENDS_TIMELINE_TASK);
        }
    }


}
