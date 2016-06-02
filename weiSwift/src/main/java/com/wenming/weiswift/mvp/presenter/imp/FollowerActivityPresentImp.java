package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.mvp.presenter.FollowerActivityPresent;
import com.wenming.weiswift.mvp.view.FollowActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FollowerActivityPresentImp implements FollowerActivityPresent {

    private UserModel mUserModel;
    private FollowActivityView mFollowActivityView;

    public FollowerActivityPresentImp(FollowActivityView followActivityView) {
        this.mFollowActivityView = followActivityView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void pullToRefreshData(long uid, Context context) {
        mFollowActivityView.showLoadingIcon();
        mUserModel.followers(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFollowActivityView.hideLoadingIcon();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFollowActivityView.hideLoadingIcon();
                mFollowActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.hideLoadingIcon();
                mFollowActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(long uid, Context context) {
        mUserModel.followersNextPage(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFollowActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFollowActivityView.hideFooterView();
                mFollowActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.showErrorFooterView();
            }
        });
    }
}
