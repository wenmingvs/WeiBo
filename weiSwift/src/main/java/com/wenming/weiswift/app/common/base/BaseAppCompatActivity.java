package com.wenming.weiswift.app.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.wenming.weiswift.R;
import com.wenming.weiswift.app.utils.ToastUtils;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setDefaultStatusBarColor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.release();
    }

    protected void setDefaultStatusBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.common_status_bar_color));
    }
}
