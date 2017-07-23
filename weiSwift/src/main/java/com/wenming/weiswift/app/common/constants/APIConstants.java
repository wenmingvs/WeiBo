package com.wenming.weiswift.app.common.constants;

/**
 * Created by wenmingvs on 2017/7/23.
 */

public class APIConstants {

    /**
     * 访问微博服务接口的地址
     */
    public static final String API_SERVER = "https://api.weibo.com/2";

    /**
     * 获取当前登录用户及其所关注（授权）用户的最新微博
     */
    public static final String FRIENDS_TIMELINE = API_SERVER + "/statuses/friends_timeline.json";

    /**
     * 获取当前登陆用户好友分组列表
     */
    public static final String GROUPS = API_SERVER + "/friendships/groups.json";
}
