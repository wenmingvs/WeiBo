package com.wenming.weiswift.app.startup.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.login.AccessTokenKeeper;
import com.wenming.weiswift.app.startup.constant.AppAuthConstants;
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
    private WeiboAuth mWeiboAuth;
    private SsoHandler mSsoHandler;

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
                    mView.showAuth();
                }
            }
        }, SPLASH_TIME);
    }

    private void initAuth() {
        if (mWeiboAuth == null) {
            mWeiboAuth = new WeiboAuth(mContext, AppAuthConstants.APP_KEY, AppAuthConstants.REDIRECT_URL, AppAuthConstants.SCOPE);
        }
    }

    @Override
    public void webAuth(Context context) {
        initAuth();
    }

    @Override
    public void ssoAuth(Activity activity) {
        initAuth();
        mSsoHandler = new SsoHandler(activity, mWeiboAuth);
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                String uid = bundle.getString("uid", "");
                String accessToken = bundle.getString("access_token", "");
                String expiresTime = bundle.getString("expires_in", "");
                String refreshToken = bundle.getString("refresh_token", "");
                if (){

                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void ssoAuthorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
