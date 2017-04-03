package com.wenming.weiswift.app.mvp.presenter.imp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.mvp.model.FriendShipModel;
import com.wenming.weiswift.app.mvp.model.UserModel;
import com.wenming.weiswift.app.mvp.model.imp.FriendShipModelImp;
import com.wenming.weiswift.app.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.app.mvp.presenter.FollowerActivityPresent;
import com.wenming.weiswift.app.mvp.view.FollowActivityView;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class FollowerActivityPresentImp implements FollowerActivityPresent {

    private UserModel mUserModel;
    private FriendShipModel friendShipModel;
    private FollowActivityView mFollowActivityView;

    public FollowerActivityPresentImp(FollowActivityView followActivityView) {
        this.mFollowActivityView = followActivityView;
        this.mUserModel = new UserModelImp();
        this.friendShipModel = new FriendShipModelImp();
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

    @Override
    public void user_destroy(final User user, final Context context, final ImageView follwerIcon, final TextView follwerText) {
        friendShipModel.user_destroy(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }
        }, true);
    }

    @Override
    public void user_create(final User user, final Context context, final ImageView follwerIcon, final TextView follwerText) {
        friendShipModel.user_create(user, context, new FriendShipModel.OnRequestListener() {
            @Override
            public void onSuccess() {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }

            @Override
            public void onError(String error) {
                mFollowActivityView.updateRealtionShip(context,user, follwerIcon, follwerText);
            }
        }, true);
    }
}
