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
import com.wenming.weiswift.utils.SDCardUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class GroupListModelImp implements GroupListModel {

    private boolean mFirstGetGroup = true;


    public void groupsOnlyOnce(Context context, OnGroupListFinishedListener onDataFinishedListener) {
        if (mFirstGetGroup) {
            groups(context, onDataFinishedListener);
        } else {
            groupsCacheLoad(context, onDataFinishedListener);
        }
    }


    private void groups(Context context, final OnGroupListFinishedListener onDataFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        groupAPI.groups(new RequestListener() {
            @Override
            public void onComplete(String response) {
                mFirstGetGroup = false;
                ArrayList<Group> groupslist = GroupList.parse(response).groupList;
                onDataFinishedListener.onDataFinish(groupslist);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                onDataFinishedListener.onError(e.getMessage());
            }
        });
    }

    @Override
    public void groupsCacheSave(Context context, String response) {
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博分组列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
    }

    @Override
    public void groupsCacheLoad(Context context, OnGroupListFinishedListener onDataFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/", "微博分组列表缓存_" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            ArrayList<Group> temp = GroupList.parse(response).groupList;
            if (temp == null || temp.size() == 0) {
                onDataFinishedListener.noMoreDate();
            } else {
                onDataFinishedListener.onDataFinish(temp);
            }
        }
    }


}
