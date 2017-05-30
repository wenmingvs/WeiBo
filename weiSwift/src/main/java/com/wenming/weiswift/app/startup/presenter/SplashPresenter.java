package com.wenming.weiswift.app.startup.presenter;

import android.content.Context;

import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.startup.contract.SplashContract;
import com.wenming.weiswift.app.startup.data.SplashDataSource;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private SplashDataSource mDataSource;
    private static final int SPLASH_TIME = 1000;
    private Context mContext;

    public SplashPresenter(Context context, SplashContract.View view, SplashDataSource dataSource) {
        this.mContext = context;
        this.mView = view;
        this.mDataSource = dataSource;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        ThreadHelper.instance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (AccessTokenKeeper.readAccessToken(mContext).isSessionValid()) {
                    mView.goToMainActivity();
                } else {
                    mView.showLogin();
                }
            }
        }, SPLASH_TIME);
    }

    @Override
    public void login(Context context) {

    }
}
