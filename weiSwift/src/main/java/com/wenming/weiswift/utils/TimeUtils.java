/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wenming.weiswift.utils;

import android.content.Context;
import android.content.res.Resources;

import com.wenming.weiswift.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/*
  credits to: Sina Weibo SDK / qii / me
*/
public class TimeUtils {
    private static final String TAG = TimeUtils.class.getSimpleName();

    private static final long MILLIS_MIN = 1000 * 60;
    private static final long MILLIS_HOUR = MILLIS_MIN * 60;

    private static String JUST_NOW, MIN, HOUR, DAY, MONTH, YEAR,
            YESTERDAY, THE_DAY_BEFORE_YESTERDAY, TODAY;

    private static SimpleDateFormat day_format = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat date_format = new SimpleDateFormat("M-d HH:mm");
    private static SimpleDateFormat year_format = new SimpleDateFormat("yyyy-M-d HH:mm");
    private static SimpleDateFormat orig_format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);

    private static Calendar sCal1 = Calendar.getInstance(),
            sCal2 = Calendar.getInstance();

    private static TimeUtils mInstance;

    private TimeUtils(Context context) {
        Resources res = context.getResources();
        JUST_NOW = res.getString(R.string.just_now);
        MIN = res.getString(R.string.min);
        HOUR = res.getString(R.string.hour);
        DAY = res.getString(R.string.day);
        MONTH = res.getString(R.string.month);
        YEAR = res.getString(R.string.year);
        YESTERDAY = res.getString(R.string.yesterday);
        THE_DAY_BEFORE_YESTERDAY = res.getString(R.string.the_day_before_yesterday);
        TODAY = res.getString(R.string.today);
    }

    public static TimeUtils instance(Context context) {
        if (mInstance == null) {
            mInstance = new TimeUtils(context);
        }

        return mInstance;
    }

    private boolean isSameDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == msgDay;
    }

    private boolean isYesterDay(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == (msgDay + 1);
    }

    private boolean isTheDayBeforeYesterday(Calendar now, Calendar msg) {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);

        return nowDay == (msgDay + 2);
    }

    private boolean isSameYear(Calendar now, Calendar msg) {
        int nowYear = now.get(Calendar.YEAR);
        int msgYear = msg.get(Calendar.YEAR);

        return nowYear == msgYear;
    }

    public synchronized long parseTimeString(String created_at) {
        try {
            return orig_format.parse(created_at).getTime();
        } catch (Exception e) {
//            if (DEBUG) {
//                Log.e(TAG, "Failed parsing time: " + created_at);
//            }

            return -1;
        }
    }

    public synchronized String buildTimeString(String created_at) {
        return buildTimeString(parseTimeString(created_at));
    }

    public synchronized String buildTimeString(long millis) {
        Calendar cal = sCal1;

        cal.setTimeInMillis(millis);

        long msg = cal.getTimeInMillis();
        long now = System.currentTimeMillis();

        Calendar nowCalendar = sCal2;
        sCal2.setTimeInMillis(now);

        long differ = now - msg;
        long difsec = differ / 1000;

        if (difsec < 60) {
            return JUST_NOW;
        }

        long difmin = difsec / 60;

        if (difmin < 60) {
            return String.valueOf(difmin) + MIN;
        }

        long difhour = difmin / 60;

        if (difhour < 24 && isSameDay(nowCalendar, cal)) {
            return TODAY + " " + day_format.format(cal.getTime());
        }

        long difday = difhour / 24;

        if (difday < 31) {
            if (isYesterDay(nowCalendar, cal)) {
                return YESTERDAY + " " + day_format.format(cal.getTime());
            } else if (isTheDayBeforeYesterday(nowCalendar, cal)) {
                return THE_DAY_BEFORE_YESTERDAY + " " + day_format.format(cal.getTime());
            } else {
                return date_format.format(cal.getTime());
            }
        }

        long difmonth = difday / 31;

        if (difmonth < 12 && isSameYear(nowCalendar, cal)) {
            return date_format.format(cal.getTime());
        }

        return year_format.format(cal.getTime());
    }
}
