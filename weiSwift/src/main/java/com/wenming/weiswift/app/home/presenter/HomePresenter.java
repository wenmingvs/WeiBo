package com.wenming.weiswift.app.home.presenter;

import com.wenming.weiswift.app.common.callback.INetWorkCallBack;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.home.contract.HomeContract;
import com.wenming.weiswift.app.home.data.HomeDataSource;

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

    }

    @Override
    public void requestGroups() {
        mDataModel.requestGroups(AccessTokenManager.getInstance().getAccessToken().getToken(), new INetWorkCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String error) {

            }
        });
    }
}
