package com.wenming.weiswift.mvp.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.wenming.weiswift.entity.User;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/16.
 */
public interface FollowActivityView {
    public void updateListView(ArrayList<User> userlist);

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
    public void showLoadFooterView();

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
     * 成功关注一个人
     */
    public void FocusSuccess(ImageView follwerIcon, TextView follwerText);

    /**
     * 关注一个人，失败
     */
    public void FocusFail(ImageView follwerIcon, TextView follwerText);

    /**
     * 成功取消关注一个人
     */
    public void disFocusSuccess(ImageView follwerIcon, TextView follwerText);


    /**
     * 取消关注一个人,失败
     */
    public void disFocusFail(ImageView follwerIcon, TextView follwerText);

}
