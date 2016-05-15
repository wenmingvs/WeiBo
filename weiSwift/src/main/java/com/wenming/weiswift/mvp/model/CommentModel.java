package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public interface CommentModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);
    }


    public void getLatestDatas(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getNextPageDatas(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getDatasFromCache(Context context, OnDataFinishedListener onDataFinishedListener);


}
