package com.wenming.weiswift.app.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by wenmingvs on 16/8/12.
 */
public class BarManager {
    /**
     * 显示顶部导航栏
     */
    public void showTopBar(final View topBar) {
        topBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * 隐藏顶部导航栏
     */
    public void hideTopBar(final ViewGroup topBar, Context context) {
        int moveOffset;
        View toast = topBar.getChildAt(1);
        moveOffset = topBar.getHeight() - toast.getHeight();
        topBar.animate().translationY(-moveOffset).setInterpolator(new AccelerateInterpolator(2));
    }

    /**
     * 显示底部导航栏
     */
    public void showBottomBar(final View bottomBar) {
        bottomBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 隐藏底部导航栏
     */
    public void hideBottomBar(final View bottomBar) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bottomBar.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomBar.animate().translationY(bottomBar.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }
}
