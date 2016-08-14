package com.wenming.weiswift.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

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

    }
}