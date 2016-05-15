package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface UserModel {

    interface OnUserTextRequestFinish {
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


    public void getUserName(long uid, Context context, OnUserTextRequestFinish onUserTextRequestFinish);

    public void getUserWeiBo(long uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void getUserWeiBoNextPage(long uid, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void getFollowers(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void getFollowersNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void getFriends(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void getFriendsNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

}
