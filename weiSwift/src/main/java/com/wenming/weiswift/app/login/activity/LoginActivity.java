package com.wenming.weiswift.app.login.activity;

import com.wenming.weiswift.app.common.base.BaseAppCompatActivity;

/**
 * Created by wenmingvs on 2017/5/30.
 */

public class LoginActivity extends BaseAppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
//    private LoginFragment mLoginFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.common_activity);
//        initFragment();
//    }
//
//    private void initFragment() {
//        mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.common_fl);
//        if (mLoginFragment == null) {
//            mLoginFragment = LoginFragment.newInstance();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.common_fl, mLoginFragment, TAG)
//                    .commit();
//        }
//        new SplashPresenter(mLoginFragment, new LoginDataManager(mContext.getApplicationContext()));
//    }
//
//    /**
//     * 监听物理返回键
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && mLoginFragment != null) {
//            mLoginFragment.onBackPress();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
