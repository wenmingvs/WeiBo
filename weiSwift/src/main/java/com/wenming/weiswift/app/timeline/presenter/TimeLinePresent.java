package com.wenming.weiswift.app.timeline.presenter;

import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.timeline.constants.Constants;
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
    private long mGroupId;
    private boolean mRefreshAll;
    private long mRefreshTime;

    public TimeLinePresent(TimeLineContract.View view, TimeLineDataSource dataModel, boolean refreshAll, long groupId) {
        this.mView = view;
        this.mDataModel = dataModel;
        this.mView.setPresenter(this);
        this.mRefreshAll = refreshAll;
        this.mGroupId = groupId;
    }

    @Override
    public void start() {
        //先加载缓存，再网络请求最新的
        loadTimeLineCache();
    }

    private void loadTimeLineCache() {
        //加载全部微博缓存
        if (mGroupId == com.wenming.weiswift.app.home.constant.Constants.GROUP_ALL) {
            mDataModel.loadFriendsTimeLineCache(Long.valueOf(AccessTokenManager.getInstance().getOAuthToken().getUid()), new LoadCacheCallBack());
        }
        //加载分组微博缓存
        else {
            mDataModel.loadGroupTimeLineCache(Long.valueOf(AccessTokenManager.getInstance().getOAuthToken().getUid()), mGroupId, new LoadCacheCallBack());
        }
    }

    @Override
    public void refreshTimeLine(List<Status> timeLineList) {
        boolean haveCacheTimeLine;
        boolean isTimeOut = false;
        if (timeLineList == null || timeLineList.size() == 0) {
            haveCacheTimeLine = false;
        } else {
            haveCacheTimeLine = true;
            isTimeOut = (System.currentTimeMillis() - mRefreshTime) >= Constants.TIME_SPACE;
        }
        //1. 外部指定，一定要全量刷新
        //2. 不存在任何微博缓存，要全量刷新
        //3. 距离上一次刷新时间超过5分钟，要全量刷新
        if (mRefreshAll || !haveCacheTimeLine || isTimeOut) {
            requestLatestTimeLine();
        }
        //请求当前第一条微博更早的微博
        else {
            requestTimeLineBySinceId(Long.valueOf(timeLineList.get(0).id));
        }
    }

    @Override
    public void loadMoreTimeLine(List<Status> timeLineList) {
        if (mGroupId == com.wenming.weiswift.app.home.constant.Constants.GROUP_ALL) {
            mDataModel.loadMoreFriendsTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), Long.valueOf(timeLineList.get(timeLineList.size() - 1).id), new LoadMoreTimeLineCallBack(this));
        } else {
            mDataModel.loadMoreGroupTimeLine(AccessTokenManager.getInstance().getOAuthToken().getToken(), mGroupId, Long.valueOf(timeLineList.get(timeLineList.size() - 1).id), new LoadMoreTimeLineCallBack(this));
        }
    }

    private void requestLatestTimeLine() {
        mRefreshTime = System.currentTimeMillis();
        mRefreshAll = false;
        if (mGroupId == com.wenming.weiswift.app.home.constant.Constants.GROUP_ALL) {
            mDataModel.refreshFriendsTimeLine(AccessTokenManager.getInstance().getUid(), AccessTokenManager.getInstance().getAccessToken(), new RefreshTimeLineCallBack(this));
        } else {
            mDataModel.refreshGroupTimeLine(AccessTokenManager.getInstance().getUid(), AccessTokenManager.getInstance().getAccessToken(), mGroupId, new RefreshTimeLineCallBack(this));
        }
    }

    private void requestTimeLineBySinceId(long sinceId) {
        mRefreshTime = System.currentTimeMillis();
        mRefreshAll = false;
        if (mGroupId == com.wenming.weiswift.app.home.constant.Constants.GROUP_ALL) {
            mDataModel.refreshFriendsTimeLine(AccessTokenManager.getInstance().getUid(), AccessTokenManager.getInstance().getAccessToken(), sinceId, new RefreshTimeLineBySinceIdCallBack(this));
        } else {
            mDataModel.refreshGroupTimeLine(AccessTokenManager.getInstance().getUid(), AccessTokenManager.getInstance().getAccessToken(), mGroupId, sinceId, new RefreshTimeLineBySinceIdCallBack(this));
        }
    }

    private static class RefreshTimeLineBySinceIdCallBack implements TimeLineDataSource.RefreshTimeLineCallBack {
        private WeakReference<TimeLinePresent> mPresenterRef;

        RefreshTimeLineBySinceIdCallBack(TimeLinePresent timeLinePresent) {
            this.mPresenterRef = new WeakReference<>(timeLinePresent);
        }

        @Override
        public void onSuccess(List<Status> statusList) {
            TimeLinePresent presenter = mPresenterRef.get();
            if (null != presenter) {
                presenter.onRefreshSuccessBySinceId(statusList);
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

    private void onRefreshSuccessBySinceId(List<Status> statusList) {
        mView.dismissLoading();
        mView.showNewWeiboCount(statusList.size());
        mView.addHeaderTimeLine(statusList);
        mView.scrollToTop();
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
        mView.showNewWeiboCount(0);
    }

    private void onRefreshSuccess(List<Status> statusList) {
        mView.dismissLoading();
        mView.showNewWeiboCount(statusList.size());
        mView.setTimeLineList(statusList);
        mView.scrollToTop();
    }

    private class LoadCacheCallBack implements TimeLineDataSource.LoadCacheCallBack {
        @Override
        public void onComplete(final List<Status> data) {
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mView.setTimeLineList(data);
                    requestLatestTimeLine();
                }
            });
        }

        @Override
        public void onEmpty() {
            ThreadHelper.instance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestLatestTimeLine();
                }
            });
        }
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
        mView.loadMoreFail();
        mView.showNetWorkTimeOut();
    }

    private void onLoadMoreNetWorkNotConnected() {
        mView.loadMoreFail();
        mView.showNetWorkNotConnected();
    }

    private void onLoadMoreFail(String error) {
        mView.showServerMessage(error);
        mView.loadMoreFail();
    }

    private void onLoadMoreEmpty() {
        mView.loadMoreEnd();
    }

    private void onLoadMoreSuccess(List<Status> statusList) {
        mView.addLastTimeLine(statusList);
        mView.loadMoreComplete();
    }
}
