package com.wenming.weiswift.app.startup.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wenming.weiswift.app.common.base.mvp.BasePresenter;
import com.wenming.weiswift.app.common.base.mvp.BaseView;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter> {

        void goToMainActivity();

        void finishActivity();

        void showAuth();
    }

    interface Presenter extends BasePresenter {

        void webAuth(Context context);

        void ssoAuth(Activity activity);

        void ssoAuthorizeCallBack(int requestCode, int resultCode, Intent data);
    }
}
