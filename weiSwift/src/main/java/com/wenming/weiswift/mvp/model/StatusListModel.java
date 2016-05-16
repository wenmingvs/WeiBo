package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface StatusListModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);
    }


    public void friendsTimeline(Context context, OnDataFinishedListener onDataFinishedListener);

    public void friendsTimelineNextPage(Context context, OnDataFinishedListener onDataFinishedListener);

    public void friendsTimelineCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener);

    public void friendsTimelineCacheSave(Context context, String response);


}
