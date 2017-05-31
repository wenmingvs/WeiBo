package com.wenming.weiswift.app.common.user;

import android.text.TextUtils;

import com.wenming.weiswift.app.common.ThreadHelper;
import com.wenming.weiswift.app.common.preference.UserAccountPrefences;

/**
 * Created by wenmingvs on 2017/5/31.
 */

public class AccessTokenKeeper {
    private static AccessTokenKeeper mInstance;

    public static AccessTokenKeeper getInstance() {
        if (mInstance == null) {
            synchronized (ThreadHelper.class) {
                if (mInstance == null) {
                    mInstance = new AccessTokenKeeper();
                }
            }
        }
        return mInstance;
    }

    public AccessTokenKeeper() {

    }

    public static boolean isTokenValid(String accessToken, long expiresTime) {
        return !TextUtils.isEmpty(accessToken) && expiresTime != 0L && System.currentTimeMillis() < expiresTime;
    }

    /**
     * 保存token信息到本地
     *
     * @param accessToken
     * @param expiresTime
     * @param uid
     */
    public void saveAuthInfo(String accessToken, long expiresTime, String uid) {
        saveAccessToken(uid, accessToken);
        setExpiresTime(uid, expiresTime);
    }

    public void saveAccessToken(String uid, String accessToken) {
        UserAccountPrefences.setAccessToken(uid, accessToken);
    }

    public String getAccessToken(String uid) {
        return UserAccountPrefences.getAccessToken(uid);
    }


    public void setExpiresTime(String uid, long expiresTime) {
        UserAccountPrefences.setExpiresTime(uid, expiresTime);
    }

    public long getExpiresTime(String uid) {
        return UserAccountPrefences.getExpiresTime(uid);
    }
}
