package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface UserModel {

    interface OnUserDetailRequestFinish {
        void onComplete(User user);

        void onError(String error);
    }


    interface OnUserListRequestFinish {
        void noMoreDate();

        void onDataFinish(ArrayList<User> userlist);

        void onError(String error);
    }

    interface OnStatusListFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> statuslist);

        void onError(String error);
    }


    public void showUserDetail(long uid, Context context, OnUserDetailRequestFinish onUserDetailRequestFinish);

    public void userTimeline(long uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimelineNextPage(long uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void followers(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void followersNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friends(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friendsNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

}
