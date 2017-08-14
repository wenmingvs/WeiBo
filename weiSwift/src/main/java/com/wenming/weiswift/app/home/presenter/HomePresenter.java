package com.wenming.weiswift.app.home.presenter;

import android.text.TextUtils;

import com.wenming.weiswift.app.common.ThreadHelper;
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
        //无token，显示空白
        if (TextUtils.isEmpty(AccessTokenManager.getInstance().getAccessToken())) {
            return;
        }
        //先load缓存再网络请求
        loadGroupsCache();
    }

    private void loadGroupsCache() {
        mDataModel.loadGroupsCache(AccessTokenManager.getInstance().getUid(), new HomeDataSource.LoadCacheCallBack() {
            @Override
            public void onComplete(final List<Group> groups) {
                ThreadHelper.instance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.setGroupsList(groups);
                        requestGroups();
                    }
                });
            }

            @Override
            public void onEmpty() {
                requestGroups();
            }
        });
    }

    @Override
    public void requestGroups() {
        mDataModel.requestGroups(AccessTokenManager.getInstance().getAccessToken(), AccessTokenManager.getInstance().getUid(), new HomeDataSource.GroupCallBack() {
            @Override
            public void onSuccess(List<Group> groups) {
                mView.setGroupsList(groups);
            }

            @Override
            public void onFail(String error) {
                mView.showServerMessage(error);
            }

            @Override
            public void onNetWorkNotConnected() {
                mView.showNoneNetWork();
            }

            @Override
            public void onTimeOut() {
                mView.dismissLoading();
            }
        });
    }
}
