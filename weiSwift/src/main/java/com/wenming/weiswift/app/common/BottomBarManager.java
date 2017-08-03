package com.wenming.weiswift.app.common;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * Created by wenmingvs on 16/8/12.
 */
public class BottomBarManager {
    /**
     * 向下滑动多少才显示起底部导航栏的阈值
     */
    public static final int SHOW_THRESHOLD = 80;

    /**
     * 向上滑动多少才收起底部导航栏的阈值
     */
    public static int HIDE_THRESHOLD = 80;

    private static BottomBarManager mInstance;
    private View mBottomView;

    public static BottomBarManager getInstance() {
        if (mInstance == null) {
            synchronized (BottomBarManager.class) {
                if (mInstance == null) {
                    mInstance = new BottomBarManager();
                }
            }
        }
        return mInstance;
    }

    public BottomBarManager() {

    }

    public void setBottomView(View view) {
        this.mBottomView = view;
    }

    /**
     * 显示底部导航栏
     */
    public void showBottomBar() {
        mBottomView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 隐藏底部导航栏
     */
    public void hideBottomBar() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mBottomView.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mBottomView.animate().translationY(mBottomView.getHeight() + fabBottomMargin).setInterpolator(new LinearInterpolator()).start();
    }

    public void release() {
        this.mBottomView = null;
    }
}
