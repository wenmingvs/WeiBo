package com.wenming.weiswift.ui.common;

import android.graphics.Color;
import android.os.Bundle;

import com.wenming.swipebacklayout.SwipeBackLayout;
import com.wenming.swipebacklayout.app.SwipeBackActivity;


/**
 * Created by wenmingvs on 2016/6/27.
 */
public class BaseActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setSwipeMode(SwipeBackLayout.FULL_SCREEN_LEFT);
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        getSwipeBackLayout().setSensitivity(BaseActivity.this, 0.3f);
        StatusBarUtils.from(this)
                .setTransparentStatusbar(true)
                .setStatusBarColor(Color.WHITE)
                .setLightStatusBar(true)
                .process(this);
    }
}