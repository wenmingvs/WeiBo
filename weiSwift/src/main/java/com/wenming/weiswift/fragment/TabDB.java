package com.wenming.weiswift.fragment;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class TabDB {

    public static String[] getTabText() {
        String[] tabs = {"首页", "消息", "New", "发现", "我"};
        return tabs;
    }

    public static int[] getTabImg() {
        int[] imags = {R.drawable.tabbar_home_auto, R.drawable.tabbar_message_auto, R.drawable.tabbar_compose_background_icon_add, R.drawable.tabbar_discover_auto, R.drawable.tabbar_profile_auto};
        return imags;
    }


    public static Class[] getFragments() {
        Class[] classess = {MainFragment.class, MessageFragment.class, PostFragment.class, DiscoverFragment.class, ProfileFragment.class};
        return classess;
    }


}
