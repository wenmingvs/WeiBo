package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface GroupListModel {

    interface OnDataFinishedListener {
        void onComplete(ArrayList<Group> groupslist);

        void onError(String error);
    }


    interface OnTimeLineListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);
    }

    public void groups(Context context, OnDataFinishedListener onDataFinishedListener);

    public void timeline(Context context, OnTimeLineListener onTimeLineListener);

    public void timelineNextPage(Context context, OnTimeLineListener onTimeLineListener);
}
