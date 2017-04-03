package com.wenming.weiswift.app.mvp.view;

import com.wenming.weiswift.app.common.entity.User;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface ProfileFragmentView {

    public void setUserDetail(User user);

    public void showScrollView();

    public void hideScrollView();

    public void showProgressDialog();

    public void hideProgressDialog();

}
