package com.wenming.weiswift.mvp.model;

import android.content.Context;

import com.wenming.weiswift.entity.Group;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public interface GroupListModel {

    interface OnDataFinishedListener {
        void onComplete(ArrayList<Group> groupslist);

        void onError(String error);
    }

    public void getGroupList(Context context, OnDataFinishedListener onDataFinishedListener);

}
