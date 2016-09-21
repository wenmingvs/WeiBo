package com.wenming.weiswift.ui.common;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import com.wenming.swipebacklayout.SwipeBackLayout;
import com.wenming.swipebacklayout.app.SwipeBackActivity;
import com.wenming.weiswift.R;
import com.wenming.weiswift.utils.SharedPreferencesUtil;


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
        boolean setNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        if (!setNightMode) {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(Color.parseColor("#FFFFFF"))
                    .setLightStatusBar(true)
                    .process(this);
        }else {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(Color.parseColor("#262626"))
                    .setLightStatusBar(true)
                    .process(this);
        }

    }
}