package com.wenming.weiswift.app.mvp.model;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/6/6.
 */
public interface FavoriteListModel {

    interface OnRequestUIListener {
        void onSuccess();

        void onError(String error);
    }

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);

    }


    public void createFavorite(Status status, Context context, OnRequestUIListener onRequestUIListener);

    public void cancelFavorite(Status status, Context context, OnRequestUIListener onRequestUIListener);

    public void favorites(Context context, OnDataFinishedListener onDataFinishedListener);

    public void favoritesNextPage(Context context, OnDataFinishedListener onDataFinishedListener);

    public void cacheSave(Context context, String response);

    public void cacheLoad(Context context, OnDataFinishedListener onDataFinishedListener);

}
