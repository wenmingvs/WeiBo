package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface StatusListModel {

    interface OnDataFinishedListener {


        /**
         * 网络请求返回数据,但是只有一条，说明没有内容了
         */
        void noMoreDate();

        /**
         * 成功请求到数据，并且完成了数据的处理了
         *
         * @param statuslist
         */
        void onDataFinish(ArrayList<Status> statuslist);


        void onError(String error);
    }

    /**
     * 获取最新的微博
     *
     * @param context
     * @param onDataFinishedListener
     */
    public void getLatestDatas(Context context, OnDataFinishedListener onDataFinishedListener);

    /**
     * 加载更多微博
     *
     * @param context
     * @param onDataFinishedListener
     */
    public void getNextPageDatas(Context context, OnDataFinishedListener onDataFinishedListener);

    public void getDatasFromCache(Context context,OnDataFinishedListener onDataFinishedListener);


}
