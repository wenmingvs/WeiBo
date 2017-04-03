package com.wenming.weiswift.app.mvp.model;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Status;

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


    public void getHotWeiBo(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getHotWeiBoNextPage(Context context, OnDataFinishedListener onDataFinishedListener);


}
