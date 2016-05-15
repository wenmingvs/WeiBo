package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.StatusListModel;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.mvp.model.imp.StatusListModelImp;
import com.wenming.weiswift.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.mvp.presenter.HomePresent;
import com.wenming.weiswift.mvp.view.HomeView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/14.
 */
public class HomePresentImp implements HomePresent {

    private HomeView mHomeView;
    private StatusListModel mStatusListModel;
    private UserModel mUserModel;


    /**
     * 构造方法
     *
     * @param homeView
     */
    public HomePresentImp(HomeView homeView) {
        this.mHomeView = homeView;
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
        mUserModel.getUserName(context, new UserModel.OnUserRequestFinish() {
            @Override
            public void onComplete(User user) {
                mHomeView.setUserName(user.name);
            }

            @Override
            public void onError(String error) {
                mHomeView.showToast(error);
                mHomeView.setUserName("我的首页");
            }
        });
    }

    /**
     * 刚进来，如果有缓存数据，而且不是第一次登录的，则不进行下拉刷新操作，否则进行下拉刷新操作
     *
     * @param context
     * @param comefromlogin
     */
    @Override
    public void firstLoadData(Context context, boolean comefromlogin) {

        if (comefromlogin) {
            pullToRefreshData(context);
            return;
        }

        mStatusListModel.getWeiBoFromCache(context, new StatusListModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHomeView.showToast("没有缓存数据");
            }

            @Override
            public void onDataFinish(ArrayList<Status> statuslist) {
                mHomeView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void requestMoreData(Context context) {
        mStatusListModel.getNextPageWeiBo(context, new StatusListModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHomeView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<Status> statuslist) {
                mHomeView.hideFooterView();
                mHomeView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mHomeView.showErrorFooterView();
                mHomeView.showToast(error);
            }
        });
    }


    /**
     * 下拉刷新操作
     *
     * @param context
     */
    @Override
    public void pullToRefreshData(Context context) {
        mHomeView.showLoadingIcon();
        mStatusListModel.getLatestWeiBo(context, new StatusListModel.OnDataFinishedListener() {
            @Override
            public void noMoreDate() {
                mHomeView.hideLoadingIcon();
                mHomeView.showToast("没有更新的内容了");
            }

            @Override
            public void onDataFinish(ArrayList<Status> statuslist) {
                mHomeView.hideLoadingIcon();
                mHomeView.updateListView(statuslist);
            }

            @Override
            public void onError(String error) {
                mHomeView.hideLoadingIcon();
                mHomeView.showErrorFooterView();
                mHomeView.showToast(error);
            }
        });
    }


}
