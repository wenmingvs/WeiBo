package com.wenming.weiswift.ui.common;

import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by wenmingvs on 2016/6/27.
 */
public class BaseSwipeActivity extends SwipeBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        //getSwipeBackLayout().setSensitivity(BaseSwipeActivity.this, 0.7f);
    }
}
