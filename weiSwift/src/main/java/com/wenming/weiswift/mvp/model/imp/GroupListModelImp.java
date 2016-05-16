package com.wenming.weiswift.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.api.GroupAPI;
import com.wenming.weiswift.entity.Group;
import com.wenming.weiswift.entity.list.GroupList;
import com.wenming.weiswift.mvp.model.GroupListModel;
import com.wenming.weiswift.ui.common.login.AccessTokenKeeper;
import com.wenming.weiswift.ui.common.login.Constants;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class GroupListModelImp implements GroupListModel {


    @Override
    public void groups(Context context, final OnDataFinishedListener onDataFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        groupAPI.groups(new RequestListener() {
            @Override
            public void onComplete(String response) {
                ArrayList<Group> groupslist = GroupList.parse(response).groupList;
                onDataFinishedListener.onComplete(groupslist);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void timeline(Context context, OnTimeLineListener onTimeLineListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));

    }

    @Override
    public void timelineNextPage(Context context, OnTimeLineListener onTimeLineListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));

    }
}
