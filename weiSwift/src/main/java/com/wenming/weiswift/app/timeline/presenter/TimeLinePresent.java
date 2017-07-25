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
        requestLatestTimeLine();
    }

    @Override
    public void requestTimeLine(List<Status> timeLineList) {
        if (timeLineList == null || timeLineList.size() == 0) {
            requestLatestTimeLine();
        } else {
            requestTimeLineBySinceId(timeLineList.get(0).id);
        }
    }

    @Override
    public void requestMoreTimeLine(List<Status> timeLineList) {

    }

    private void requestLatestTimeLine() {
        mDataModel.requestLatestTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), new TimeLineDataSource.TimeLinePullToRefreshCallBack() {
            @Override
            public void onSuccess(List<Status> statusList) {
                mView.dismissLoading();
                mView.addHeaderTimeLine(statusList);
            }

            @Override
            public void onPullToRefreshEmpty() {
                mView.dismissLoading();
                mView.showPullToRefreshEmpty();
            }

            @Override
            public void onFail(String error) {
                mView.dismissLoading();
                mView.showServerMessage(error);
            }

            @Override
            public void onNetWorkNotConnected() {
                mView.dismissLoading();
                mView.showNetWorkNotConnected();
            }

            @Override
            public void onTimeOut() {
                mView.dismissLoading();
                mView.showTimeOut();
            }
        });
    }

    private void requestTimeLineBySinceId(String sinceId) {
        mDataModel.requestLatestTimeLineBySinceId(AccessTokenManager.getInstance().getOAuthToken().getToken(), sinceId, new TimeLineDataSource.TimeLinePullToRefreshCallBack() {
            @Override
            public void onSuccess(List<Status> statusList) {

            }

            @Override
            public void onPullToRefreshEmpty() {

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
}
