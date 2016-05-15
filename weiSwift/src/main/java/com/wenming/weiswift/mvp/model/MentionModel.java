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


    public void getLatestMention(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getNextPageMention(Context context, OnDataFinishedListener onDataFinishedListener);

    public void saveMentionCache(Context context, String response);

    public ArrayList<Status> getMentionCache(Context context);
}
