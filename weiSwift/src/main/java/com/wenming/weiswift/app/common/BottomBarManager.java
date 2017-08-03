package com.wenming.weiswift.app.common;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by wenmingvs on 16/8/12.
 */
public class BottomBarManager {
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBottomView.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mBottomView.animate().translationY(mBottomView.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void release() {
        this.mBottomView = null;
    }
}
