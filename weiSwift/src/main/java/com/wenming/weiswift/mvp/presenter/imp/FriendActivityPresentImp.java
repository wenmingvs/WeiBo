package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.mvp.presenter.FriendActivityPresent;
import com.wenming.weiswift.mvp.view.FriendActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FriendActivityPresentImp implements FriendActivityPresent {

    private UserModel mUserModel;
    private FriendActivityView mFriendActivityView;

    public FriendActivityPresentImp(FriendActivityView friendActivityView) {
        this.mFriendActivityView = friendActivityView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void pullToRefreshData(long uid, Context context) {
        mFriendActivityView.showLoadingIcon();
        mUserModel.friends(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFriendActivityView.hideLoadingIcon();

            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.hideLoadingIcon();
                mFriendActivityView.showErrorFooterView();
            }
        });
    }

    @Override
    public void requestMoreData(long uid, Context context) {
        mUserModel.friendsNextPage(uid, context, new UserModel.OnUserListRequestFinish() {
            @Override
            public void noMoreDate() {
                mFriendActivityView.showEndFooterView();
            }

            @Override
            public void onDataFinish(ArrayList<User> userlist) {
                mFriendActivityView.hideFooterView();
                mFriendActivityView.updateListView(userlist);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.showErrorFooterView();
            }
        });
    }
}
