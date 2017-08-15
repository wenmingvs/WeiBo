package com.wenming.weiswift.app.timeline.constants;

/**
 * Created by wenmingvs on 2017/7/24.
 */

public class Constants {
    public static final int TYPE_ORINGIN_ITEM = 0;
    public static final int TYPE_ORINGIN_DELETE = 1;
    public static final int TYPE_RETWEET_ITEM = 2;

    public static final long TIMELINE_DEFALUT_SINCE_ID = 0;
    public static final long TIMELINE_DEFALUT_MAX_ID = 0;
    public static final int TIMELINE_DEFALUT_COUNT = 20;
    public static final int TIMELINE_DEFALUT_PAGE = 1;
    public static final int TIMELINE_DEFALUT_FEATURE = 0;
    public static final boolean TIMELINE_DEFALUT_TRIM_USER = false;

    /**
     * 第一条微博的时间发布超过3分钟，要全量刷新
     */
    public static final int TIME_SPACE = 10 * 60 * 1000;
}
