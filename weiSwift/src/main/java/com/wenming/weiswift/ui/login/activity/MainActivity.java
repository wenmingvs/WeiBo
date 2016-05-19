package com.wenming.weiswift.ui.login.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wenming.weiswift.R;
import com.wenming.weiswift.ui.common.MyApplication;
import com.wenming.weiswift.ui.login.fragment.discovery.DiscoverFragment;
import com.wenming.weiswift.ui.login.fragment.home.HomeFragment;
import com.wenming.weiswift.ui.login.fragment.message.MessageFragment;
import com.wenming.weiswift.ui.login.fragment.post.PostActivity;
import com.wenming.weiswift.ui.login.fragment.profile.ProfileFragment;
import com.wenming.weiswift.utils.LogUtil;


public class MainActivity extends FragmentActivity {

    private static final int HOME_FRAGMENT = 0X001;
    private static final int MESSAGE_FRAGMENT = 0X002;
    private static final int DISCOVERY_FRAGMENT = 0X004;
    private static final int PROFILE_FRAGMENT = 0X005;

    private int mCurrentIndex;
    private Context mContext;
    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;


    private FragmentManager mFragmentManager;
    private RelativeLayout mHomeTab, mMessageTab, mDiscoeryTab, mProfile;
    private FrameLayout mPostTab;

    private boolean mComeFromAccoutActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_layout);
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
        mHomeTab = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTab = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscoeryTab = (RelativeLayout) findViewById(R.id.tv_discovery);
        mProfile = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTab = (FrameLayout) findViewById(R.id.fl_post);
        mContext = this;
        mFragmentManager = getSupportFragmentManager();
        setTabFragment(HOME_FRAGMENT);
        setUpListener();
    }

    private void setUpListener() {
        mHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(HOME_FRAGMENT);
                if (mCurrentIndex == HOME_FRAGMENT && mHomeFragment != null) {
                    mHomeFragment.scrollToTop(false);
                }
            }
        });
        mMessageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(MESSAGE_FRAGMENT);
            }
        });

        mPostTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        mDiscoeryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(DISCOVERY_FRAGMENT);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(PROFILE_FRAGMENT);
                if (mCurrentIndex == PROFILE_FRAGMENT && mProfileFragment != null && mProfileFragment.haveAlreadyRefresh()) {
                    mProfileFragment.refreshUserDetail(mContext, false);
                }
            }
        });
    }


    private void setTabFragment(int index) {
        if (mCurrentIndex != index) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            hideAllFragments(transaction);
            switch (index) {
                case HOME_FRAGMENT:
                    mHomeTab.setSelected(true);
                    if (mHomeFragment == null) {
                        mHomeFragment = new HomeFragment(mComeFromAccoutActivity);
                        transaction.add(R.id.contentLayout, mHomeFragment);
                    } else {
                        transaction.show(mHomeFragment);
                    }
                    mCurrentIndex = HOME_FRAGMENT;
                    break;
                case MESSAGE_FRAGMENT:
                    mMessageTab.setSelected(true);
                    if (mMessageFragment == null) {
                        mMessageFragment = new MessageFragment();
                        transaction.add(R.id.contentLayout, mMessageFragment);
                    } else {
                        transaction.show(mMessageFragment);
                    }
                    mCurrentIndex = MESSAGE_FRAGMENT;
                    break;

                case DISCOVERY_FRAGMENT:
                    mDiscoeryTab.setSelected(true);
                    if (mDiscoverFragment == null) {
                        mDiscoverFragment = new DiscoverFragment();
                        transaction.add(R.id.contentLayout, mDiscoverFragment);
                    } else {
                        transaction.show(mDiscoverFragment);
                    }
                    mCurrentIndex = DISCOVERY_FRAGMENT;
                    break;
                case PROFILE_FRAGMENT:
                    mProfile.setSelected(true);
                    if (mProfileFragment == null) {
                        mProfileFragment = new ProfileFragment();
                        transaction.add(R.id.contentLayout, mProfileFragment);
                    } else {
                        transaction.show(mProfileFragment);
                    }
                    mCurrentIndex = PROFILE_FRAGMENT;
                    break;
            }
            transaction.commit();
        }
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mProfileFragment != null) {
            transaction.hide(mProfileFragment);
        }
        mHomeTab.setSelected(false);
        mMessageTab.setSelected(false);
        mDiscoeryTab.setSelected(false);
        mProfile.setSelected(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mHomeFragment != null) {
            mHomeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("确定要退出？")
                    .setCancelable(true)
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((MyApplication) getApplication()).finishAll();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("MainActivity被销毁");
        super.onDestroy();
    }
}
