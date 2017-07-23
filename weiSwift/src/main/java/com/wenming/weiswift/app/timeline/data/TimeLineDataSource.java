package com.wenming.weiswift.app.timeline.data;

import com.wenming.weiswift.app.common.entity.Status;

import java.util.List;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public interface TimeLineDataSource {
    void requestTimeLine(String accessToken, TimeLineCallBack callBack);

    interface TimeLineCallBack {
        void onSuccess(List<Status> statusList);

        void onFail(String error);

        void onNetWorkNotConnected();

        void onTimeOut();
    }
}
