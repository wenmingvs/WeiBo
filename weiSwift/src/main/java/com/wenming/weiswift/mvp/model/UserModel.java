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

    interface OnUserDeleteListener {
        void onSuccess(ArrayList<User> userlist);

        void onEmpty();

        void onError(String error);
    }


    public void show(long uid, Context context, OnUserDetailRequestFinish onUserDetailRequestFinish);

    public void show(String screenName, Context context, OnUserDetailRequestFinish onUserDetailRequestFinish);

    public User showUserDetailSync(long uid, Context context);

    public void userTimeline(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimeline(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userPhoto(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimelineNextPage(long uid, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userTimelineNextPage(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void userPhotoNextPage(String screenName, int groupId, Context context, OnStatusListFinishedListener onStatusFinishedListener);

    public void followers(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void followersNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friends(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void friendsNextPage(long uid, Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void getUserDetailList(Context context, OnUserListRequestFinish onUserListRequestFinish);

    public void deleteUserByUid(long uid, Context context, OnUserDeleteListener onUserDeleteListener);

    public void cacheSave_statuslist(int groupType, Context context, String response);

    public void cacheLoad_statuslist(int groupType, Context context, OnStatusListFinishedListener onStatusListFinishedListener);

    public void cacheSave_user(Context context, String response);

    public void cacheLoad_user(Context context, OnUserDetailRequestFinish onUserDetailRequestFinish);

    public void cacheSave_userlist(int groupType, Context context, String response);

    public void cacheLoad_userlist(int groupType, Context context, OnUserListRequestFinish onUserListRequestFinish);

}


