package com.wenming.weiswift.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.R;
import com.wenming.weiswift.common.AccessTokenKeeper;
import com.wenming.weiswift.home.fragment.DiscoverFragment;
import com.wenming.weiswift.home.fragment.MainFragment;
import com.wenming.weiswift.home.fragment.MessageFragment;
import com.wenming.weiswift.home.fragment.PostFragment;
import com.wenming.weiswift.home.fragment.ProfileFragment;
import com.wenming.weiswift.home.util.ActivityCollector;
import com.wenming.weiswift.home.util.NewFeature;


public class MainActivity extends FragmentActivity {
    private Context mContext;
    private MainFragment mMainFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;
    private PostFragment mPostFragment;
    private FragmentManager mFragmentManager;
    private Oauth2AccessToken mAccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainactivity_layout);
        mContext = this;
        ActivityCollector.addActivity(this);
        setSessionValid();
        mFragmentManager = getSupportFragmentManager();
        setTabFragment(0);
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
            case 0:
                if (mMainFragment == null) {
                    mMainFragment = new MainFragment();
                    transaction.add(R.id.contentLayout, mMainFragment);
                } else {
                    transaction.show(mMainFragment);
                }
                break;
            case 1:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    transaction.add(R.id.contentLayout, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case 2:
                if (mPostFragment == null) {
                    mPostFragment = new PostFragment();
                    transaction.add(R.id.contentLayout, mPostFragment);
                } else {
                    transaction.show(mPostFragment);
                }
                break;
            case 3:
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new DiscoverFragment();
                    transaction.add(R.id.contentLayout, mDiscoverFragment);
                } else {
                    transaction.show(mDiscoverFragment);
                }
                break;
            case 4:
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
    }


//    private void initTab() {
//        mFragmentTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
//        mFragmentTabHost.setup(mContext, getSupportFragmentManager(), R.id.contentLayout);
//        mFragmentTabHost.getTabWidget().setDividerDrawable(null);
//        TabHost.TabSpec tabSpec;
//        String tabs[] = TabDB.getTabText();
//        for (int i = 0; i < tabs.length; i++) {
//            tabSpec = mFragmentTabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
//            mFragmentTabHost.addTab(tabSpec, TabDB.getFragments()[i], null);
//            mFragmentTabHost.setTag(i);
//        }
//        mMainFragMent = (MainFragment) getSupportFragmentManager().findFragmentByTag("首页");
//        mMessageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag("消息");
//        mPostFragment = (PostFragment) getSupportFragmentManager().findFragmentByTag("New");
//        mDiscoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag("发现");
//        mProfileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("我");
//    }
//
//
//    private View getTabView(int index) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.tabitem_havetext, null);
//        TextView textView = (TextView) view.findViewById(R.id.itemTextView);
//        textView.setText(TabDB.getTabText()[index]);
//        Drawable drawable = getResources().getDrawable(TabDB.getTabImg()[index]);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        textView.setCompoundDrawables(null, drawable, null, null);
//        return view;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("首页");
        if (mMainFragment != null) {
            mMainFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
