package com.wenming.weiswift.app.home.presenter;

import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.home.contract.HomeContract;
import com.wenming.weiswift.app.home.data.HomeDataSource;
import com.wenming.weiswift.app.home.data.entity.Group;

import java.util.List;

/**
 * Created by wenmingvs on 2017/6/5.
 */

public class HomePresenter implements HomeContract.Presenter {
    private HomeDataSource mDataModel;
    private HomeContract.View mView;

    public HomePresenter(HomeDataSource dataModel, HomeContract.View view) {
        this.mDataModel = dataModel;
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        requestGroups();
    }

    @Override
    public void requestGroups() {
        mView.showLoading();
        mDataModel.requestGroups(AccessTokenManager.getInstance().getOAuthToken().getToken(), new HomeDataSource.GroupCallBack() {
            @Override
            public void onSuccess(List<Group> groups) {
                mView.dismissLoading();
                mView.setGroupsList(groups);
            }

            @Override
            public void onFail(String error) {
                mView.dismissLoading();
                mView.showServerMessage(error);
            }

            @Override
            public void onNetWorkNotConnected() {
                mView.dismissLoading();
                mView.showNoneNetWork();
            }

            @Override
            public void onTimeOut() {
                mView.dismissLoading();
            }
        });
    }
}
