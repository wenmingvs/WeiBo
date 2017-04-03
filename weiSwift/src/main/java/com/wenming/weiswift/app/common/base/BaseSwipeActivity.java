package com.wenming.weiswift.app.common.base;

import android.os.Bundle;

import com.wenming.swipebacklayout.SwipeBackLayout;
import com.wenming.swipebacklayout.app.SwipeBackActivity;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.StatusBarUtils;
import com.wenming.weiswift.utils.SharedPreferencesUtil;


/**
 * Created by wenmingvs on 2016/6/27.
 */
public class BaseSwipeActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setSwipeMode(SwipeBackLayout.FULL_SCREEN_LEFT);
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        getSwipeBackLayout().setSensitivity(BaseSwipeActivity.this, 0.3f);
        boolean isNightMode = (boolean) SharedPreferencesUtil.get(this, "setNightMode", false);
        if (!isNightMode) {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(getResources().getColor(R.color.home_status_bg))
                    .setLightStatusBar(true)
                    .process(this);
        } else {
            StatusBarUtils.from(this)
                    .setTransparentStatusbar(true)
                    .setStatusBarColor(getResources().getColor(R.color.home_status_bg))
                    .process(this);
        }

    }
}