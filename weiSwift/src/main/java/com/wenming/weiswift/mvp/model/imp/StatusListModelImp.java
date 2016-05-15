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
    private TimerTask mTimeTask;
    private static final int refreshAllDateTime = 15 * 60 * 1000;

    @Override
    public void getLatestWeiBo(final Context context, final OnDataFinishedListener onRequestFinishedListener) {
        setTimeTask();
        if (mStatusesAPI == null) {
            mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }

        if (mRefrshAllData) {
            mFirstWeiboId = 0;
        }
        mStatusesAPI.friendsTimeline(mFirstWeiboId, 0, NewFeature.GET_WEIBO_NUMS, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Status> temp = StatusList.parse(response).statusList;
                if (temp != null && temp.size() > 0) {
                    if (mStatusList != null) {
                        mStatusList.clear();
                    }
                    saveWeiBoCache(context, response);
                    mStatusList = temp;
                    updateId();
                    onRequestFinishedListener.onDataFinish(mStatusList);
                } else {
                    ToastUtil.showShort(context, "没有更新的内容了");
                    onRequestFinishedListener.noMoreDate();
                }
                mRefrshAllData = false;
            }

            @Override
            public void onWeiboException(WeiboException e) {
                ToastUtil.showShort(context, e.getMessage());
            }
        });
    }

    @Override
    public void getNextPageWeiBo(final Context context, final OnDataFinishedListener onRequestFinishedListener) {
        setTimeTask();
        if (mStatusesAPI == null) {
            mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        }
        if (mNoMoreData == true) {
            return;
        }
        mStatusesAPI.friendsTimeline(0, mLastWeiboId, NewFeature.LOADMORE_WEIBO_ITEM, 1, false, 0, false, new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    ArrayList<Status> temp = StatusList.parse(response).statusList;
                    if (temp.size() == 0 || (temp != null && temp.size() == 1 && temp.get(0).id.equals(String.valueOf(mLastWeiboId)))) {
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
                ToastUtil.showShort(context, e.getMessage());
                onRequestFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void getWeiBoFromCache(Context context, OnDataFinishedListener onDataFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            ArrayList<Status> temp = StatusList.parse(response).statusList;
            if (temp == null || temp.size() == 0) {
                onDataFinishedListener.noMoreDate();
            } else {
                mStatusList = temp;
                updateId();
                onDataFinishedListener.onDataFinish(mStatusList);
            }
        }

    }

    @Override
    public void saveWeiBoCache(Context context, String response) {
        if (NewFeature.CACHE_WEIBOLIST) {
            SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
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
