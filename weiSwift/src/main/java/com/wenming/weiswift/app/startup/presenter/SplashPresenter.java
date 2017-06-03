package com.wenming.weiswift.app.startup.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.oauth.AccessTokenManager;
import com.wenming.weiswift.app.common.oauth.constant.AppAuthConstants;
import com.wenming.weiswift.app.startup.contract.SplashContract;
import com.wenming.weiswift.app.startup.data.SplashDataSource;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private static final String BUDDLE_KEY_UID = "uid";
    private static final String BUDDLE_KEY_ACCESS_TOKEN = "access_token";
    private static final String BUDDLE_KEY_EXPIRES_IN = "expires_in";
    private static final String BUDDLE_KEY_REFRESH_TOKEN = "refresh_token";
    private SplashContract.View mView;
    private SplashDataSource mDataSource;
    private static final int SPLASH_TIME = 500;
    private Context mContext;
    private AuthInfo mWeiboAuth;
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
                if (AccessTokenManager.getInstance().getAccessToken() != null && AccessTokenManager.getInstance().getAccessToken().isSessionValid()) {
                    mView.goToMainActivity();
                } else {
                    mView.showAuth();
                }
            }
        }, SPLASH_TIME);
    }

    private void initAuth() {
        if (mWeiboAuth == null) {
            mWeiboAuth = new AuthInfo(mContext, AppAuthConstants.APP_KEY, AppAuthConstants.REDIRECT_URL, AppAuthConstants.SCOPE);
        }
    }

    @Override
    public void webAuth(Context context) {
        initAuth();
        mView.goToWebAuthActivity();
    }

    @Override
    public void ssoAuth(Activity activity) {
        initAuth();
        mSsoHandler = new SsoHandler(activity,mWeiboAuth);
        mSsoHandler.authorizeClientSso(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                String uid = bundle.getString(BUDDLE_KEY_UID, "");
                String accessToken = bundle.getString(BUDDLE_KEY_ACCESS_TOKEN, "");
                String expiresIn = bundle.getString(BUDDLE_KEY_EXPIRES_IN, "");
                String refreshToken = bundle.getString(BUDDLE_KEY_REFRESH_TOKEN, "");
                Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
                oauth2AccessToken.setUid(uid);
                oauth2AccessToken.setToken(accessToken);
                oauth2AccessToken.setExpiresIn(expiresIn);
                oauth2AccessToken.setRefreshToken(refreshToken);
                AccessTokenManager.getInstance().writeAccessToken(oauth2AccessToken);
                mView.showAuthSuccess();
                mView.goToMainActivity();
                mView.finishActivity();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                mView.showAuthError(e.getMessage());
            }

            @Override
            public void onCancel() {
                mView.showCancel();
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
