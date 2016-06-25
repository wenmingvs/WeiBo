package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Comment;
import com.wenming.weiswift.entity.Status;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/6/25.
 */
public interface StatusDetailModel {

    interface OnCommentCallBack {
        void noMoreDate();

        void onDataFinish(ArrayList<Comment> commentlist);

        void onError(String error);
    }


    interface OnRepostCallBack {
        void noMoreDate();

        void onDataFinish(ArrayList<Status> commentlist);

        void onError(String error);
    }

    public void comment(int groupType, Status status, Context context, OnCommentCallBack onCommentCallBack);

    public void commentNextPage(int groupType, Status status, Context context, OnCommentCallBack onCommentCallBack);


    public void repost(int groupType, Status status, Context context, OnRepostCallBack onRepostCallBack);

    public void repostNextPage(int groupType, Status status, Context context, OnRepostCallBack onRepostCallBack);

}
