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

    public static final String AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";// @人
    public static final String TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";// ##话题
    public static final String URL = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    public static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情
    public static final String ALL = "(" + AT + ")" + "|" + "(" + TOPIC + ")" + "|" + "(" + URL + ")" + "|" + "(" + EMOJI + ")";

    public static final String SHROT_URL = "http://t.cn/[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    public static final String HTML_VIDEO_IMG = "imgUrl: [a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";

    /**
     * 第一条微博的时间发布超过3分钟，要全量刷新
     */
    public static final int TIME_SPACE = 10 * 60 * 1000;
}
