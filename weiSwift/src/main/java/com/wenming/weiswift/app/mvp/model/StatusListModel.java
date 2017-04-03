package com.wenming.weiswift.app.mvp.model;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.list.StatusList;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface StatusListModel {

    interface OnDataFinishedListener {
        void noMoreData();

        void noDataInFirstLoad(String error);

        void getNewWeiBo(int num);

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);
    }

    interface OnRequestListener {
        void onSuccess();

        void onError(String error);
    }

    public void timeline(long groundId, Context context, OnDataFinishedListener onDataFinishedListener);

    public void friendsTimeline(Context context, OnDataFinishedListener onDataFinishedListener);

    public void bilateralTimeline(Context context, OnDataFinishedListener onDataFinishedListener);

    public void weibo_destroy(long id, Context context, OnRequestListener onRequestListener);

    public void friendsTimelineNextPage(Context context, OnDataFinishedListener onDataFinishedListener);

    public void bilateralTimelineNextPage(Context context, OnDataFinishedListener onDataFinishedListener);

    public void timelineNextPage(long groundId, Context context, OnDataFinishedListener onDataFinishedListener);

    public void setRefrshFriendsTimelineTask();

    public void cancelTimer();

    public boolean cacheLoad(long groupType, Context context, OnDataFinishedListener onDataFinishedListener);

    public void cacheSave(long groupType, Context context, StatusList statusList);


}
