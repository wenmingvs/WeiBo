package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.mvp.model.FriendShipModel;
import com.wenming.weiswift.app.mvp.model.UserModel;
import com.wenming.weiswift.app.mvp.model.imp.FriendShipModelImp;
import com.wenming.weiswift.app.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.app.mvp.presenter.FriendActivityPresent;
import com.wenming.weiswift.app.mvp.view.FriendActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FriendActivityPresentImp implements FriendActivityPresent {

    private UserModel mUserModel;
    private FriendShipModel friendShipModel;
    private FriendActivityView mFriendActivityView;

    public FriendActivityPresentImp(FriendActivityView friendActivityView) {
        this.mFriendActivityView = friendActivityView;
        this.mUserModel = new UserModelImp();
        this.friendShipModel = new FriendShipModelImp();
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

    @Override
    public void user_destroy(final User user, final Context context, final ImageView friendIcon, final TextView friendText) {
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
                //mFriendActivityView.disFocusSuccess(friendIcon, friendText);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }
        }, false);
    }

    @Override
    public void user_create(final User user, final Context context, final ImageView friendIcon, final TextView friendText) {
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }

            @Override
            public void onError(String error) {
                mFriendActivityView.updateRealtionShip(context,user, friendIcon, friendText);
            }
        }, false);
    }

}
