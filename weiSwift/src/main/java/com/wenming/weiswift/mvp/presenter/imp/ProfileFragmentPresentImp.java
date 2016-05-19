package com.wenming.weiswift.mvp.presenter.imp;

import android.content.Context;

import com.wenming.weiswift.entity.User;
import com.wenming.weiswift.mvp.model.UserModel;
import com.wenming.weiswift.mvp.model.imp.UserModelImp;
import com.wenming.weiswift.mvp.presenter.ProfileFragmentPresent;
import com.wenming.weiswift.mvp.view.ProfileFragmentView;

/**
 * Created by wenmingvs on 16/5/16.
 */
public class ProfileFragmentPresentImp implements ProfileFragmentPresent {

    private UserModel mUserModel;
    private ProfileFragmentView mProfileFragmentView;

    public ProfileFragmentPresentImp(ProfileFragmentView profileFragmentView) {
        this.mProfileFragmentView = profileFragmentView;
        this.mUserModel = new UserModelImp();
    }

    @Override
    public void refreshUserDetail(long uid, Context context, boolean loadIcon) {
        if (loadIcon) {
            mProfileFragmentView.showProgressDialog();
        }
        mUserModel.showUserDetail(uid, context, new UserModel.OnUserDetailRequestFinish() {
            @Override
            public void onComplete(User user) {
                mProfileFragmentView.showScrollView();
                mProfileFragmentView.hideProgressDialog();
                mProfileFragmentView.setUserDetail(user);
            }

            @Override
            public void onError(String error) {
                mProfileFragmentView.showScrollView();
                mProfileFragmentView.hideProgressDialog();
            }
        });
    }
}
