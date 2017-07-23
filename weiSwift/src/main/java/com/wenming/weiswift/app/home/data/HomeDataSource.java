package com.wenming.weiswift.app.home.data;

import com.wenming.weiswift.app.home.data.entity.Group;

import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public interface HomeDataSource {
    void requestGroups(String accessToken, GroupCallBack callBack);

    interface GroupCallBack {
        void onSuccess(List<Group> data);

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
