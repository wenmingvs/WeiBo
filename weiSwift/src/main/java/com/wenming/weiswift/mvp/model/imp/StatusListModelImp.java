package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
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
    private boolean mRefrshFriendsTimeline;
    private TimerTask mRefrshFriendsTimelineTask;
    
    @Override
    public void friendsTimeline(final Context context, final OnDataFinishedListener onDataFinishedListener) {
        setRefrshFriendsTimelineTask();
        StatusesAPI mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        long sinceId = 0;
        if (mFriendsTimeline.size() > 0) {
            sinceId = Long.valueOf(mFriendsTimeline.get(0).id);
        }
        if (mRefrshFriendsTimeline) {
            sinceId = 0;
        }
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
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onDataFinishedListener.noMoreDate();
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
                        onRequestFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mFriendsTimeline.addAll(temp);
                        onRequestFinishedListener.onDataFinish(mFriendsTimeline);
                    }
                } else {
                    onRequestFinishedListener.noMoreDate();
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
                onDataFinishedListener.noMoreDate();
            } else {
                mFriendsTimeline = temp;
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
