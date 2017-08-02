package com.wenming.weiswift.app.timeline.presenter;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.timeline.contract.TimeLineContract;
import com.wenming.weiswift.app.timeline.data.TimeLineDataSource;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class TimeLinePresent implements TimeLineContract.Presenter {
    private TimeLineContract.View mView;
    private TimeLineDataSource mDataModel;
    private long mGourpId;

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
    public void refreshTimeLine(List<Status> timeLineList) {
        //请求最新的微博
        if (timeLineList == null || timeLineList.size() == 0) {
            requestLatestTimeLine();
        }
        //请求当前第一条微博更早的微博
        else {
            requestTimeLineBySinceId(timeLineList.get(0).id);
        }
    }

    @Override
    public void loadMoreTimeLine(List<Status> timeLineList) {
        mDataModel.loadMoreTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), timeLineList.get(timeLineList.size() - 1).id, new LoadMoreTimeLineCallBack(this));
    }

    private void requestLatestTimeLine() {
        mDataModel.refreshTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), new RefreshTimeLineCallBack(this));
    }

    private void requestTimeLineBySinceId(String sinceId) {
        mDataModel.refreshTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), sinceId, new RefreshTimeLineCallBack(this));
    }

    private static class RefreshTimeLineCallBack implements TimeLineDataSource.RefreshTimeLineCallBack {
        private WeakReference<TimeLinePresent> mPresenterRef;

        RefreshTimeLineCallBack(TimeLinePresent timeLinePresent) {
            this.mPresenterRef = new WeakReference<>(timeLinePresent);
        }

        @Override
        public void onSuccess(List<Status> statusList) {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshSuccess(statusList);
            }
        }

        @Override
        public void onPullToRefreshEmpty() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshEmpty();
            }
        }

        @Override
        public void onFail(String error) {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshFail(error);
            }
        }

        @Override
        public void onNetWorkNotConnected() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshNetWorkNotConnected();
            }
        }

        @Override
        public void onTimeOut() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshTimeOut();
            }
        }
    }

    private void onRefreshTimeOut() {
        mView.dismissLoading();
        mView.showNetWorkTimeOut();
    }

    private void onRefreshNetWorkNotConnected() {
        mView.dismissLoading();
        mView.showNetWorkNotConnected();
    }

    private void onRefreshFail(String error) {
        mView.dismissLoading();
        mView.showServerMessage(error);
    }

    private void onRefreshEmpty() {
        mView.dismissLoading();
        mView.showPullToRefreshEmpty();
    }

    private void onRefreshSuccess(List<Status> statusList) {
        mView.dismissLoading();
        mView.addHeaderTimeLine(statusList);
        mView.scrollToTop();
    }

    private class LoadMoreTimeLineCallBack implements TimeLineDataSource.LoadMoreTimeLineCallBack {
        private WeakReference<TimeLinePresent> mPresenterRef;

        LoadMoreTimeLineCallBack(TimeLinePresent timeLinePresent) {
            this.mPresenterRef = new WeakReference<>(timeLinePresent);
        }

        @Override
        public void onLoadMoreSuccess(List<Status> statusList) {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onLoadMoreSuccess(statusList);
            }
        }

        @Override
        public void onLoadMoreEmpty() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onLoadMoreEmpty();
            }
        }

        @Override
        public void onFail(String error) {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onLoadMoreFail(error);
            }
        }

        @Override
        public void onNetWorkNotConnected() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onLoadMoreNetWorkNotConnected();
            }
        }

        @Override
        public void onTimeOut() {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onLoadMoreTimeOut();
            }
        }
    }

    private void onLoadMoreTimeOut() {
        mView.showNetWorkTimeOut();
    }

    private void onLoadMoreNetWorkNotConnected() {
        mView.showNetWorkNotConnected();
    }

    private void onLoadMoreFail(String error) {
        mView.showServerMessage(error);
    }

    private void onLoadMoreEmpty() {

    }

    private void onLoadMoreSuccess(List<Status> statusList) {
        mView.addLastTimeLine(statusList);
    }
}
