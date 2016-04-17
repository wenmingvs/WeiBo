package com.wenming.weiswift.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.NewFeature;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.AccessTokenKeeper;
import com.wenming.weiswift.common.ActivityCollector;
import com.wenming.weiswift.discovery.fragment.DiscoverFragment;
import com.wenming.weiswift.home.fragment.MainFragment;
import com.wenming.weiswift.message.fragment.MessageFragment;
import com.wenming.weiswift.post.fragment.PostFragment;
import com.wenming.weiswift.profile.fragment.ProfileFragment;


public class MainActivity extends FragmentActivity {

    private static final int HOME_FRAGMENT = 0X001;
    private static final int MESSAGE_FRAGMENT = 0X002;
    private static final int POST_FRAGMENT = 0X003;
    private static final int DISCOVERY_FRAGMENT = 0X004;
    private static final int PROFILE_FRAGMENT = 0X005;
    private Context mContext;
    private MainFragment mMainFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;
    private PostFragment mPostFragment;
    private FragmentManager mFragmentManager;
    private Oauth2AccessToken mAccessToken;
    private TextView mHomeTab, mMessageTab, mDiscoeryTab, mProfile;
    private FrameLayout mPostTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainactivity_layout);
        mHomeTab = (TextView) findViewById(R.id.tv_home);
        mMessageTab = (TextView) findViewById(R.id.tv_message);
        mDiscoeryTab = (TextView) findViewById(R.id.tv_discovery);
        mProfile = (TextView) findViewById(R.id.tv_profile);
        mPostTab = (FrameLayout) findViewById(R.id.fl_post);
        mContext = this;
        ActivityCollector.addActivity(this);
        setSessionValid();
        mFragmentManager = getSupportFragmentManager();
        setTabFragment(HOME_FRAGMENT);
        setUpListener();
    }

    private void setUpListener() {
        mHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setTabFragment(HOME_FRAGMENT);

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
                setTabFragment(POST_FRAGMENT);
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
            }
        });
    }

    private void setSessionValid() {
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (mAccessToken.isSessionValid()) {
            NewFeature.LOGIN_STATUS = true;
        } else {
            NewFeature.LOGIN_STATUS = false;
        }
    }

    private void setTabFragment(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFragments(transaction);

        switch (index) {
            case HOME_FRAGMENT:
                mHomeTab.setSelected(true);
                if (mMainFragment == null) {
                    mMainFragment = new MainFragment();
                    transaction.add(R.id.contentLayout, mMainFragment);
                } else {
                    transaction.show(mMainFragment);
                }
                break;
            case MESSAGE_FRAGMENT:
                mMessageTab.setSelected(true);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.contentLayout, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case POST_FRAGMENT:
                mPostTab.setSelected(true);
                if (mPostFragment == null) {
                    mPostFragment = new PostFragment();
                    transaction.add(R.id.contentLayout, mPostFragment);
                } else {
                    transaction.show(mPostFragment);
                }
                break;
            case DISCOVERY_FRAGMENT:
                mDiscoeryTab.setSelected(true);
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new DiscoverFragment();
                    transaction.add(R.id.contentLayout, mDiscoverFragment);
                } else {
                    transaction.show(mDiscoverFragment);
                }
                break;
            case PROFILE_FRAGMENT:
                mProfile.setSelected(true);
                if (mProfileFragment == null) {
                    mProfileFragment = new ProfileFragment();
                    transaction.add(R.id.contentLayout, mProfileFragment);
                } else {
                    transaction.show(mProfileFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mMainFragment != null) {
            transaction.hide(mMainFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }
        if (mPostFragment != null) {
            transaction.hide(mPostFragment);
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
        if (mMainFragment != null) {
            mMainFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
