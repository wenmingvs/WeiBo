package com.wenming.weiswift.app.common.oauth;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.preference.UserAccountPrefences;

/**
 * Created by wenmingvs on 2017/5/31.
 */

public class AccessTokenManager {
    private static AccessTokenManager mInstance;
    private static Oauth2AccessToken sAccessToken;

    public static AccessTokenManager getInstance() {
        if (mInstance == null) {
            synchronized (ThreadHelper.class) {
                if (mInstance == null) {
                    mInstance = new AccessTokenManager();
                }
            }
        }
        return mInstance;
    }

    private AccessTokenManager() {
        initData();
    }

    private void initData() {
        String uid = UserAccountPrefences.getUid();
        String accessToken = UserAccountPrefences.getAccessToken();
        String refreshToken = UserAccountPrefences.getRefreshAccessToken();
        long expiresTime = UserAccountPrefences.getExpiresTime();
        sAccessToken = new Oauth2AccessToken(accessToken, String.valueOf(expiresTime));
        sAccessToken.setUid(uid);
        sAccessToken.setRefreshToken(refreshToken);
        sAccessToken.setExpiresTime(expiresTime);
    }

    public void writeAccessToken(Oauth2AccessToken token) {
        if (null == token) {
            return;
        }
        sAccessToken = token;
        UserAccountPrefences.setUid(token.getUid());
        UserAccountPrefences.setAccessToken(token.getToken());
        UserAccountPrefences.setRefreshAccessToken(token.getRefreshToken());
        UserAccountPrefences.setExpiresTime(token.getExpiresTime());
    }

    public Oauth2AccessToken getOAuthToken() {
        return sAccessToken;
    }

    public long getUid() {
        return Long.valueOf(sAccessToken.getUid());
    }

    public String getAccessToken() {
        return sAccessToken.getToken();
    }

    public String getRefreshToken() {
        return sAccessToken.getRefreshToken();
    }

    public long getExpiresTime() {
        return sAccessToken.getExpiresTime();
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     */
    public void clearAccessToken() {
        UserAccountPrefences.setUid("");
        UserAccountPrefences.setAccessToken("");
        UserAccountPrefences.setRefreshAccessToken("");
        UserAccountPrefences.setExpiresTime(-1);
    }

}
