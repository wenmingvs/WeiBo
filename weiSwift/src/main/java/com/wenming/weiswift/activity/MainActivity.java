package com.wenming.weiswift.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.DiscoverFragment;
import com.wenming.weiswift.fragment.FragmentTabHost;
import com.wenming.weiswift.fragment.MainFragment;
import com.wenming.weiswift.fragment.MessageFragment;
import com.wenming.weiswift.fragment.PostFragment;
import com.wenming.weiswift.fragment.ProfileFragment;
import com.wenming.weiswift.fragment.TabDB;
import com.wenming.weiswift.util.ActivityCollector;
import com.wenming.weiswift.util.NewFeature;
import com.wenming.weiswift.util.androidutils.DensityUtil;
import com.wenming.weiswift.weiboAccess.AccessTokenKeeper;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabHost;
    private Context mContext;
    private MainFragment mMainFragMent;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;
    private PostFragment mPostFragment;
    private Oauth2AccessToken mAccessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainactivity_layout);
        mContext = this;
        ActivityCollector.addActivity(this);
        initTab();
        setSessionValid();
    }

    private void setSessionValid() {
        mAccessToken = AccessTokenKeeper.readAccessToken(mContext);
        if (mAccessToken.isSessionValid()) {
            NewFeature.LOGIN_STATUS = true;
        } else {
            NewFeature.LOGIN_STATUS = false;
        }


    }

    private void initTab() {
        mFragmentTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mFragmentTabHost.setup(mContext, getSupportFragmentManager(), R.id.contentLayout);
        mFragmentTabHost.getTabWidget().setDividerDrawable(null);
        TabHost.TabSpec tabSpec;
        String tabs[] = TabDB.getTabText();
        for (int i = 0; i < tabs.length; i++) {
            tabSpec = mFragmentTabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
            mFragmentTabHost.addTab(tabSpec, TabDB.getFragments()[i], null);
            mFragmentTabHost.setTag(i);
        }
        mMainFragMent = (MainFragment) getSupportFragmentManager().findFragmentByTag("首页");
        mMessageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag("消息");
        mPostFragment = (PostFragment) getSupportFragmentManager().findFragmentByTag("New");
        mDiscoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag("发现");
        mProfileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("我");


    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tabitem_havetext, null);
        TextView textView = (TextView) view.findViewById(R.id.itemTextView);
        textView.setText(TabDB.getTabText()[index]);
        Drawable drawable = getResources().getDrawable(TabDB.getTabImg()[index]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
        return view;
    }

    private View getMiddleTabView(int index) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tabitem_notext, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dp2px(mContext, 60), DensityUtil.dp2px(mContext, 45));
        params.setMargins(0, DensityUtil.dp2px(mContext, 5), 0, DensityUtil.dp2px(mContext, 5));
        view.setLayoutParams(params);
        return view;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment mainFragment = getSupportFragmentManager().findFragmentByTag("首页");
        if (mainFragment != null) {
            mainFragment.onActivityResult(requestCode, resultCode, data);

//            android.support.v4.app.FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
//            MainFragment mainFragment1 = new MainFragment();
//            tr.replace(R.id.contentLayout,mainFragment1);
//            tr.commit();
            // getSupportFragmentManager.().getFragments().remove(0);
            // getSupportFragmentManager().getFragments().add(mainFragment);
//            getSupportFragmentManager().beginTransaction().attach(mainFragment);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
