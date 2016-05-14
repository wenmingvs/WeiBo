package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.User;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface UserModel {

    interface OnUserRequestFinish {
        void onComplete(User user);

        void onError(String error);
    }


    public void getUserName(Context context, OnUserRequestFinish onUserRequestFinish);

}
