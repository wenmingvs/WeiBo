package com.wenming.weiswift.app.common.preference;

import android.content.SharedPreferences;

import com.wenming.weiswift.app.common.MyApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wenmingvs on 2017/5/31.
 */

public class UserPrefences {

    private static final String FILE_NAME_SUFFIX = "_user_preference";
    //用户分组信息
    private static final String ACCOUNT_GOURPS = "account_groups";

    private static SharedPreferences getSp(long uid) {
        return MyApplication.getContext().getSharedPreferences(uid + FILE_NAME_SUFFIX, MODE_PRIVATE);
    }

    public static void setUserGroups(long uid, String jsonString) {
        setString(uid, ACCOUNT_GOURPS, jsonString);
    }

    public static String getUserGroups(long uid) {
        return getSp(uid).getString(ACCOUNT_GOURPS, "");
    }

    protected static void setString(long uid, String key, String value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putString(key, value);
        settingsEditor.apply();
    }

    protected static void setBoolean(long uid, String key, boolean value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putBoolean(key, value);
        settingsEditor.apply();
    }

    protected static void setInt(long uid, String key, int value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putInt(key, value);
        settingsEditor.apply();
    }

    protected static void setFloat(long uid, String key, float value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putFloat(key, value);
        settingsEditor.apply();
    }

    protected static void setLong(long uid, String key, long value) {
        final SharedPreferences.Editor settingsEditor = getSp(uid).edit();
        settingsEditor.putLong(key, value);
        settingsEditor.apply();
    }
}
