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

import com.wenming.weiswift.R;
import com.wenming.weiswift.fragment.DiscoverFragment;
import com.wenming.weiswift.fragment.FragmentTabHost;
import com.wenming.weiswift.fragment.MainFragment;
import com.wenming.weiswift.fragment.MessageFragment;
import com.wenming.weiswift.fragment.PostFragment;
import com.wenming.weiswift.fragment.ProfileFragment;
import com.wenming.weiswift.fragment.TabDB;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabHost;
    private Context mContext;
    private MainFragment mMainFragMent;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;
    private PostFragment mPostFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.mainactivity_layout);
        mContext = this;
        initTab();

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
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("首页");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
//        mMainFragMent.onDestroy();
//        mMessageFragment.onDestroy();
//        mPostFragment.onDestroy();
//        mDiscoverFragment.onDestroy();
//        mProfileFragment.onDestroy();
        super.onDestroy();
    }
}
