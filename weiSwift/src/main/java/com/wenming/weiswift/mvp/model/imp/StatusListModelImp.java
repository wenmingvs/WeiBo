package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
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
    private StatusesAPI mStatusesAPI;
    private ArrayList<Status> mStatusList = new ArrayList<>();
    private long mFirstWeiboId;
    private long mLastWeiboId;
    private boolean mNoMoreData;
    private boolean mRefrshAllData;

    private static final int refreshAllDateTime = 15 * 60 * 1000;

    private TimerTask mTimeTask;

    @Override
    public void getLatestDatas(final Context context, final OnDataFinishedListener onRequestFinishedListener) {
        setTimeTask();
        mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        if (mRefrshAllData) {
            mFirstWeiboId = 0;
        }
        ToastUtil.showShort(context, mFirstWeiboId + "");
        mStatusesAPI.friendsTimeline(mFirstWeiboId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mStatusList != null) {
                        mStatusList.clear();
                    }
                    if (NewFeature.CACHE_WEIBOLIST) {
                        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
                    }
                    mStatusList = temp;
                    updateId();
                    onRequestFinishedListener.onDataFinish(mStatusList);
                } else {
                    onRequestFinishedListener.noMoreDate();
                }
                mRefrshAllData = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRequestFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void getNextPageDatas(final Context context, final OnDataFinishedListener onRequestFinishedListener) {
        setTimeTask();
        ToastUtil.showShort(context, mLastWeiboId + "");
        mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mStatusesAPI.friendsTimeline(0, mLastWeiboId, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp != null && temp.size() == 1 && temp.get(0).id.equals(mStatusList.get(mStatusList.size() - 1).id)) {
                        mNoMoreData = true;
                        onRequestFinishedListener.noMoreDate();
                    } else if (temp.size() > 1) {
                        temp.remove(0);
                        mStatusList.addAll(temp);
                        updateId();
                        onRequestFinishedListener.onDataFinish(mStatusList);
                    }
                } else {
                    onRequestFinishedListener.noMoreDate();
                    mNoMoreData = true;
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onRequestFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void getDatasFromCache(Context context, OnDataFinishedListener onDataFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        ArrayList<Status> temp = StatusList.parse(response).statusList;
        if (temp == null || temp.size() == 0) {
            onDataFinishedListener.noMoreDate();
        } else {
            mStatusList = temp;
            updateId();
            onDataFinishedListener.onDataFinish(mStatusList);
        }

    }


    public void setTimeTask() {
        if (mTimeTask == null) {
            mTimeTask = new TimerTask() {
                @Override
                public void run() {
                    mRefrshAllData = true;
                }
            };
            new Timer().schedule(mTimeTask, 0, refreshAllDateTime);
        }
    }

    public void updateId() {
        mFirstWeiboId = Long.valueOf(mStatusList.get(0).id);
        mLastWeiboId = Long.valueOf(mStatusList.get(mStatusList.size() - 1).id);
    }

}
