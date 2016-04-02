package com.wenming.weiswift.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenmingvs on 2016/1/7.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
