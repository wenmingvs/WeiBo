package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface HotWeiBoModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);

    }


    public void getLatestComment(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getNextPageComment(Context context, OnDataFinishedListener onDataFinishedListener);


}
