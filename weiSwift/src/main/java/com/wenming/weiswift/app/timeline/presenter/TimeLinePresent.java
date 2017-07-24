package com.wenming.weiswift.app.timeline.presenter;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.timeline.contract.TimeLineContract;
import com.wenming.weiswift.app.timeline.data.TimeLineDataSource;

import java.util.List;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class TimeLinePresent implements TimeLineContract.Presenter {

    private TimeLineContract.View mView;
    private TimeLineDataSource mDataModel;

    public TimeLinePresent(TimeLineContract.View view, TimeLineDataSource dataModel) {
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        requestTimeLine();
    }

    @Override
    public void requestTimeLine() {
        mDataModel.requestTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), new TimeLineDataSource.TimeLineCallBack() {
            @Override
            public void onSuccess(List<Status> statusList) {
                mView.setTimeLineList(statusList);
            }

            @Override
            public void onFail(String error) {
                
            }

            @Override
            public void onNetWorkNotConnected() {

            }

            @Override
            public void onTimeOut() {

            }
        });
    }

    @Override
    public void requestHeaderTimeLine() {

    }

    @Override
    public void requestLastTimeLine() {

    }
}
