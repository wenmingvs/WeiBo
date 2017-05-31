package com.wenming.weiswift.app.common.preference;

import android.content.SharedPreferences;

import com.wenming.weiswift.app.common.MyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class UserAccountPrefences {
    private static final String SP_NAME_SUFFIX = "_account_sp";
    private static final String ACCOUNT_ACCESS_TOKEN = "_account_access_token";
    private static final String ACCOUNT_EXPIRES_TIME = "_account_expires_time";


    public static void setAccessToken(String uid, String accessToken) {
        setString(uid, ACCOUNT_ACCESS_TOKEN, accessToken);
    }

    public static String getAccessToken(String uid) {
        return getSp(uid).getString(ACCOUNT_ACCESS_TOKEN, "");
    }

    public static void setExpiresTime(String uid, long expires) {
        setLong(uid, ACCOUNT_EXPIRES_TIME, expires);
    }

    public static long getExpiresTime(String uid) {
        return getSp(uid).getLong(ACCOUNT_EXPIRES_TIME, 0);
    }

    private static SharedPreferences getSp(String uid) {
        String spName = uid + SP_NAME_SUFFIX;
        return MyApplication.getContext().getSharedPreferences(uid, MODE_PRIVATE);
    }

    protected static void setString(String uid, String key, String value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putString(key, value);
        settingsEditor.apply();
    }

    protected static void setBoolean(String uid, String key, boolean value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putBoolean(key, value);
        settingsEditor.apply();
    }

    protected static void setInt(String uid, String key, int value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putInt(key, value);
        settingsEditor.apply();
    }

    protected static void setFloat(String uid, String key, float value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putFloat(key, value);
        settingsEditor.apply();
    }

    protected static void setLong(String uid, String key, long value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putLong(key, value);
        settingsEditor.apply();
    }
}
