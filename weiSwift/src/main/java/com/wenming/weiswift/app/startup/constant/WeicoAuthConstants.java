package com.wenming.weiswift.app.startup.constant;

import android.text.TextUtils;

/**
 * Created by wenmingvs on 2017/5/31.
 */

public class WeicoAuthConstants {
    public static final String APP_KEY = "211160679";
    public static final String REDIRECT_URL = "http://oauth.weico.cc";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static final String AppSecret = "1e6e33db08f9192306c4afa0a61ad56c";
    public static final String PackageName = "com.eico.weico";

    public static final String AUTHURL = "https://open.weibo.cn/oauth2/authorize?"
            + "client_id=" + APP_KEY
            + "&response_type=token&redirect_uri=" + REDIRECT_URL
            + "&key_hash=" + AppSecret + (TextUtils.isEmpty(PackageName) ? "" : "&packagename=" + PackageName)
            + "&display=mobile" + "&scope=" + SCOPE;
}
