package com.wenming.weiswift.app.mvp.model.imp;

import android.content.Context;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wenming.weiswift.app.api.GroupAPI;
import com.wenming.weiswift.app.common.entity.Group;
import com.wenming.weiswift.app.common.entity.list.GroupList;
import com.wenming.weiswift.app.mvp.model.GroupListModel;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.login.Constants;
import com.wenming.weiswift.utils.SDCardUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class GroupListModelImp implements GroupListModel {
    private ArrayList<Group> mGroupList;
    private boolean mFirstGetGroup = true;
    private Context mContext;
    private OnGroupListFinishedListener mOnGroupListFinishedListener;

    public void groupsOnlyOnce(Context context, OnGroupListFinishedListener onGroupListFinishedListener) {
        if (mFirstGetGroup) {
            groups(context, onGroupListFinishedListener);
        } else {
            cacheLoad(context, onGroupListFinishedListener);
        }
    }


    private void groups(final Context context, final OnGroupListFinishedListener onGroupListFinishedListener) {
        GroupAPI groupAPI = new GroupAPI(context, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(context));
        mContext = context;
        mOnGroupListFinishedListener = onGroupListFinishedListener;
        groupAPI.groups(mGroupRequestListener);
    }

    @Override
    public void cacheLoad(Context context, OnGroupListFinishedListener onGroupListFinishedListener) {
        String response = SDCardUtil.get(context, SDCardUtil.getSDCardPath() + "/weiSwift/other", "我的分组列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt");
        if (response != null) {
            mGroupList = GroupList.parse(response).lists;
            onGroupListFinishedListener.onDataFinish(mGroupList);
        }
    }

    @Override
    public void cacheSave(Context context, String response) {
        SDCardUtil.put(context, SDCardUtil.getSDCardPath() + "/weiSwift/other", "我的分组列表" + AccessTokenKeeper.readAccessToken(context).getUid() + ".txt", response);
    }

    public RequestListener mGroupRequestListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            mFirstGetGroup = false;
            cacheSave(mContext, response);
            ArrayList<Group> groupslist = GroupList.parse(response).lists;
            mOnGroupListFinishedListener.onDataFinish(groupslist);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mOnGroupListFinishedListener.onError(e.getMessage());
            cacheLoad(mContext, mOnGroupListFinishedListener);
        }
    };
}
