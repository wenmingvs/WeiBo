package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.app.common.entity.Status;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.mvp.model.StatusListModel;
import com.wenming.weiswift.app.mvp.model.UserModel;
import com.wenming.weiswift.app.mvp.model.imp.StatusListModelImp;
import com.wenming.weiswift.app.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.app.mvp.presenter.HomeFragmentPresent;
import com.wenming.weiswift.app.mvp.view.HomeFragmentView;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.login.Constants;
import com.wenming.weiswift.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class HomeFragmentPresentImp implements HomeFragmentPresent {

    private HomeFragmentView mHomeFragmentView;
    private StatusListModel mStatusListModel;
    private UserModel mUserModel;

    private static final int GROUP_TYPE_ALL = 0;
    private static final int GROUP_TYPE_FRIENDS_CIRCLE = 1;


    /**
     * 构造方法
     *
     * @param homeView
     */
    public HomeFragmentPresentImp(HomeFragmentView homeView) {
        this.mHomeFragmentView = homeView;
        this.mStatusListModel = new StatusListModelImp();
        this.mUserModel = new UserModelImp();
    }


    /**
     * 获取UserName
     *
     * @param context
     */
    @Override
    public void refreshUserName(Context context) {
        LogUtil.d(AccessTokenKeeper.readAccessToken(context).getUid());
        mUserModel.show(Long.valueOf(AccessTokenKeeper.readAccessToken(context).getUid()), context, new UserModel.OnUserDetailRequestFinish() {
            @Override
            public void onComplete(User user) {
                mHomeFragmentView.setCurrentUser(user);
                mHomeFragmentView.setGroupName(user.name);
                mHomeFragmentView.setUserName(user.name);
                mHomeFragmentView.popWindowsDestory();
            }

            @Override
            public void onError(String error) {
                mHomeFragmentView.setGroupName("我的首页");
            }
        });
    }

    /**
     * 刚进来，如果有缓存数据，而且不是第一次登录的，则不进行下拉刷新操作，否则进行下拉刷新操作
     *
     * @param context
     * @param comefromlogin 刚刚登录成功，本地并没有缓存
     */
    @Override
    public void firstLoadData(Context context, boolean comefromlogin) {
        if (comefromlogin) {
            mHomeFragmentView.showLoadingIcon();
            mStatusListModel.friendsTimeline(context, onPullFinishedListener);
        } else {
            //加载本地缓存失败，则做请求操作
            if (mStatusListModel.cacheLoad(Constants.GROUP_TYPE_ALL, context, onPullFinishedListener) == false) {
                mHomeFragmentView.showLoadingIcon();
                mStatusListModel.friendsTimeline(context, onPullFinishedListener);
            }
        }
    }

    @Override
    public void pullToRefreshData(long groupId, Context context) {
        mHomeFragmentView.showLoadingIcon();
        if (groupId == GROUP_TYPE_ALL) {
            mStatusListModel.friendsTimeline(context, onPullFinishedListener);
        } else if (groupId == GROUP_TYPE_FRIENDS_CIRCLE) {
            mStatusListModel.bilateralTimeline(context, onPullFinishedListener);
        } else {
            mStatusListModel.timeline(groupId, context, onPullFinishedListener);
        }
    }

    @Override
    public void requestMoreData(long groupId, Context context) {
        if (groupId == GROUP_TYPE_ALL) {
            mStatusListModel.friendsTimelineNextPage(context, onRequestMoreFinishedListener);
        } else if (groupId == GROUP_TYPE_FRIENDS_CIRCLE) {
            mStatusListModel.bilateralTimelineNextPage(context, onRequestMoreFinishedListener);
        } else {
            mStatusListModel.timelineNextPage(groupId, context, onRequestMoreFinishedListener);
        }
    }

    @Override
    public void cancelTimer() {
        mStatusListModel.cancelTimer();
    }


    public StatusListModel.OnDataFinishedListener onPullFinishedListener = new StatusListModel.OnDataFinishedListener() {
        @Override
        public void noMoreData() {
            mHomeFragmentView.hideLoadingIcon();
            mHomeFragmentView.showRecyclerView();
            mHomeFragmentView.hideEmptyBackground();
        }

        @Override
        public void noDataInFirstLoad(String text) {
            mHomeFragmentView.hideLoadingIcon();
            mHomeFragmentView.hideRecyclerView();
            mHomeFragmentView.showEmptyBackground(text);
        }

        @Override
        public void getNewWeiBo(int num) {
            mHomeFragmentView.showOrangeToast(num);
        }

        @Override
        public void onDataFinish(ArrayList<Status> statuslist) {
            mHomeFragmentView.hideLoadingIcon();
            mHomeFragmentView.scrollToTop(false);
            mHomeFragmentView.updateListView(statuslist);
            mHomeFragmentView.showRecyclerView();
            mHomeFragmentView.hideEmptyBackground();
        }

        @Override
        public void onError(String error) {
            mHomeFragmentView.hideLoadingIcon();
            mHomeFragmentView.showRecyclerView();
            mHomeFragmentView.showErrorFooterView();
            mHomeFragmentView.hideEmptyBackground();
        }
    };

    public StatusListModel.OnDataFinishedListener onRequestMoreFinishedListener = new StatusListModel.OnDataFinishedListener() {
        @Override
        public void noMoreData() {
            mHomeFragmentView.showEndFooterView();
        }

        @Override
        public void noDataInFirstLoad(String text) {
        }

        @Override
        public void getNewWeiBo(int num) {

        }

        @Override
        public void onDataFinish(ArrayList<Status> statuslist) {
            mHomeFragmentView.hideFooterView();
            mHomeFragmentView.updateListView(statuslist);
        }

        @Override
        public void onError(String error) {
            mHomeFragmentView.showErrorFooterView();
        }
    };
}
