package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;

/**
 * Created by wenmingvs on 16/6/6.
 */
public interface FavoriteListModel {

    interface OnRequestUIListener {
        void onSuccess();

        void onError(String error);
    }


    public void createFavorite(Status status, Context context, OnRequestUIListener onRequestUIListener);

    public void cancelFavorite(Status status, Context context, OnRequestUIListener onRequestUIListener);

}
