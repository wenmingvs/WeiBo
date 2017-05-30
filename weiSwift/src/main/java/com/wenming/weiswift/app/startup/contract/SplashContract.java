package com.wenming.weiswift.app.startup.contract;

import android.content.Context;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter> {

        void goToMainActivity();

        void finishActivity();

        void showLogin();
    }

    interface Presenter extends BasePresenter {

        void login(Context context);
    }
}
