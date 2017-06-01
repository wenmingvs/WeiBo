package com.wenming.weiswift.app.common.oauth;

import android.text.TextUtils;

/**
 * Created by wenmingvs on 2017/6/1.
 */

public class AccessToken {
    private String mUid;
    private String mAccessToken;
    private String mRefreshToken;
    private long mExpiresTime;

    public AccessToken(String accessToken, long expiresTime) {
        this.mAccessToken = accessToken;
        this.mExpiresTime = expiresTime;
    }

    public boolean isSessionValid() {
        return !TextUtils.isEmpty(this.mAccessToken) && this.mExpiresTime != 0L && System.currentTimeMillis() < this.mExpiresTime;
    }

    public String toString() {
        return "uid: " + this.mUid + ", " + "access_token" + ": " + this.mAccessToken + ", " + "refresh_token" + ": "
                + this.mRefreshToken + ", " + "expires_in" + ": " + Long.toString(this.mExpiresTime);
    }

    public String getUid() {
        return this.mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getToken() {
        return this.mAccessToken;
    }

    public void setToken(String token) {
        this.mAccessToken = token;
    }

    public String getRefreshToken() {
        return this.mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.mRefreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return this.mExpiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.mExpiresTime = expiresTime;
    }

    public static long parseExpiresIn2Time(String expiresIn) {
        if (!TextUtils.isEmpty(expiresIn) && !expiresIn.equals("0")) {
            return System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000L;
        } else {
            return 0;
        }
    }
}
