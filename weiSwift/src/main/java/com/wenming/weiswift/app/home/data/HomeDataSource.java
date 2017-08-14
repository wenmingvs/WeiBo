package com.wenming.weiswift.app.home.data;

import com.wenming.weiswift.app.home.data.entity.Group;

import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public interface HomeDataSource {

    void loadGroupsCache(long uid, LoadCacheCallBack callBack);

    void requestGroups(String accessToken,long uid, GroupCallBack callBack);

    interface LoadCacheCallBack {
        void onComplete(List<Group> data);

        void onEmpty();
    }

    interface GroupCallBack {
        void onSuccess(List<Group> data);

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
