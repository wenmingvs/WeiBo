package com.wenming.weiswift.app.startup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseAppCompatActivity;
import com.wenming.weiswift.app.login.activity.LoginActivity;
import com.wenming.weiswift.app.startup.data.SplashDataManager;
import com.wenming.weiswift.app.startup.fragment.SplashFragment;
import com.wenming.weiswift.app.startup.presenter.SplashPresenter;

/**
 * Created by wenmingvs on 16/5/4.
 */
public class SplashActivity extends BaseAppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
    private SplashFragment mSplashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);
        initFragment();
    }

    private void initFragment() {
        mSplashFragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.common_fl);
        if (mSplashFragment == null) {
            mSplashFragment = SplashFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.common_fl, mSplashFragment, TAG)
                    .commit();
        }
        new SplashPresenter(mContext, mSplashFragment, new SplashDataManager());
    }

    /**
     * 监听物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mSplashFragment != null) {
            mSplashFragment.onBackPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSplashFragment.onActivityResult(requestCode, resultCode, data);
    }
}
