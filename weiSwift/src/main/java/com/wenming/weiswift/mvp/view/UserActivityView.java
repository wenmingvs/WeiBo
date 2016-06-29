package com.wenming.weiswift.mvp.view;

import com.wenming.weiswift.entity.Status;
import com.wenming.weiswift.entity.User;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2016/6/28.
 */
public interface UserActivityView {

    public void updateStatusListView(ArrayList<Status> statuselist, boolean resetAdapter);

    public void updatePhotoListView(ArrayList<Status> statuselist, boolean resetAdapter);

    public void updateUserInfoListView(User user, boolean resetAdapter);


    /**
     * 显示loading动画
     */
    public void showLoadingIcon();

    /**
     * 隐藏loadding动画
     */
    public void hideLoadingIcon();

    /**
     * 显示正在加载的FooterView
     */
    public void showLoadFooterView(String currentgroup);

    /**
     * 隐藏FooterView
     */
    public void hideFooterView();

    /**
     * 显示FooterView，提示没有任何内容了
     */
    public void showEndFooterView();

    /**
     * 显示FooterView，提示没有网络
     */
    public void showErrorFooterView();

    /**
     * 滑动到顶部
     */
    public void restoreScrollOffset(boolean refreshData);
}
