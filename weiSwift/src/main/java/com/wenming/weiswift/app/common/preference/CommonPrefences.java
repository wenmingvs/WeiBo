package com.wenming.weiswift.app.common.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.wenming.weiswift.app.common.ApplicationHelper;

/**
 * Created by wenmingvs on 2017/5/31.
 */

public class CommonPrefences {
    private static final String FILE_NAME = "common_preference";

    private static SharedPreferences getSp() {
        return ApplicationHelper.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    private static void setString(String key, String value) {
        final SharedPreferences.Editor settingsEditor = getSp().edit();
        settingsEditor.putString(key, value);
        settingsEditor.apply();
    }

    public static void setBoolean(String key, boolean value) {
        final SharedPreferences.Editor settingsEditor = getSp().edit();
        settingsEditor.putBoolean(key, value);
        settingsEditor.apply();
    }

    private static void setInt(String key, int value) {
        final SharedPreferences.Editor settingsEditor = getSp().edit();
        settingsEditor.putInt(key, value);
        settingsEditor.apply();
    }

    private static void setLong(String key, long value) {
        final SharedPreferences.Editor settingsEditor = getSp().edit();
        settingsEditor.putLong(key, value);
        settingsEditor.apply();
    }

    private static void setFloat(String key, float value) {
        final SharedPreferences.Editor settingsEditor = getSp().edit();
        settingsEditor.putFloat(key, value);
        settingsEditor.apply();
    }
}
