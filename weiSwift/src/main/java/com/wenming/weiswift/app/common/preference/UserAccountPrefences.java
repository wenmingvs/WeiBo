package com.wenming.weiswift.app.common.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.wenming.weiswift.app.common.MyApplication;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class UserAccountPrefences {
    private static final String SP_NAME_SUFFIX = "_account_sp";
    private static final String ACCOUNT_UID = "account_uid";
    private static final String ACCOUNT_ACCESS_TOKEN = "account_access_token";
    private static final String ACCOUNT_REFRESH_TOKEN = "account_refresh_token";
    private static final String ACCOUNT_EXPIRES_TIME = "account_expires_time";

    public static void setUid(String uid) {
        setString(ACCOUNT_UID, uid);
    }

    public static String getUid() {
        return getSp().getString(ACCOUNT_UID, "");
    }

    public static void setAccessToken(String accessToken) {
        setString(ACCOUNT_ACCESS_TOKEN, accessToken);
    }

    public static String getAccessToken() {
        return getSp().getString(ACCOUNT_ACCESS_TOKEN, "");
    }

    public static void setRefreshAccessToken(String refreshToken) {
        setString(ACCOUNT_REFRESH_TOKEN, refreshToken);
    }

    public static String getRefreshAccessToken() {
        return getSp().getString(ACCOUNT_REFRESH_TOKEN, "");
    }

    public static void setExpiresTime(long expires) {
        setLong(ACCOUNT_EXPIRES_TIME, expires);
    }

    public static long getExpiresTime() {
        return getSp().getLong(ACCOUNT_EXPIRES_TIME, 0);
    }

    private static final String FILE_NAME = "user_account_preference";

    private static SharedPreferences getSp() {
        return MyApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    private static void setString(String key, String value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static void setInt(String key, int value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static void setLong(String key, long value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    private static void setFloat(String key, float value) {
        SharedPreferences.Editor editor = getSp().edit();
        editor.putFloat(key, value);
        editor.apply();
    }
}
