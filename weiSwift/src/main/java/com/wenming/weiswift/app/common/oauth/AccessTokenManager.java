package com.wenming.weiswift.app.common.oauth;

import android.content.Context;

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
        String accessToken = UserAccountPrefences.getAccessToken();
        long expiresTime = UserAccountPrefences.getExpiresTime();
        sAccessToken = new Oauth2AccessToken(accessToken, String.valueOf(expiresTime));
        sAccessToken.setExpiresTime(expiresTime);
    }

    public static void writeAccessToken(Oauth2AccessToken token) {
        if (null == token) {
            return;
        }
        sAccessToken = token;
        UserAccountPrefences.setUid(token.getUid());
        UserAccountPrefences.setAccessToken(token.getToken());
        UserAccountPrefences.setRefreshAccessToken(token.getRefreshToken());
        UserAccountPrefences.setExpiresTime(token.getExpiresTime());
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken getAccessToken() {
        return sAccessToken;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     */
    public static void clearAccessToken() {
        UserAccountPrefences.setUid("");
        UserAccountPrefences.setAccessToken("");
        UserAccountPrefences.setRefreshAccessToken("");
        UserAccountPrefences.setExpiresTime(-1);
    }

}
