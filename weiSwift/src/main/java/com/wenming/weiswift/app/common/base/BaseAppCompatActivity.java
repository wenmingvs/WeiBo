package com.wenming.weiswift.app.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.StatusBarUtils;
import com.wenming.weiswift.app.utils.ToastUtils;
import com.wenming.weiswift.utils.SharedPreferencesUtil;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.release();
    }

    private void initStatusBar() {
        if (!(boolean) SharedPreferencesUtil.get(this, "setNightMode", false)) {
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
