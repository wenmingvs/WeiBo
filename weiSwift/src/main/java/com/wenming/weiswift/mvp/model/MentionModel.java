package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public interface MentionModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);

    }

    public void mentions(int groupType, Context context, OnDataFinishedListener onDataFinishedListener);

    public void mentionsNextPage(int groupType, Context context, OnDataFinishedListener onDataFinishedListener);

    public void mentionCacheSave(Context context, String response);

    public void mentionCacheLoad(Context context, OnDataFinishedListener onDataFinishedListener);
}
