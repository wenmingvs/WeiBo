package com.wenming.weiswift.app.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenming.weiswift.R;
import com.wenming.weiswift.app.common.base.BaseFragment;
import com.wenming.weiswift.app.home.adapter.GroupPagerAdapter;
import com.wenming.weiswift.app.home.weibo.fragment.WeiBoFragment;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends BaseFragment {
    private Toolbar mToolBar;
    private TabLayout mGourpTl;
    private ViewPager mGroupVp;
    private AppCompatActivity mActivity;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView();
        initView();
    }


    private void prepareView() {
        mActivity = (AppCompatActivity) getActivity();
        mToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        mGourpTl = (TabLayout) findViewById(R.id.main_tablayout);
        mGroupVp = (ViewPager) findViewById(R.id.main_groups_vp);
    }

    private void initView() {
        initToolBar();
        initTabViewPager();
    }

    private void initToolBar() {
        mActivity.setSupportActionBar(mToolBar);
    }

    private void initTabViewPager() {
        GroupPagerAdapter adapter = new GroupPagerAdapter(mActivity.getSupportFragmentManager());
        adapter.addFragment(new WeiBoFragment(), getString(R.string.main_tab_default));
        adapter.addFragment(new WeiBoFragment(), getString(R.string.main_tab_default));
        mGroupVp.setAdapter(adapter);
        mGourpTl.setupWithViewPager(mGroupVp);
        //默认展示第一个
        mGroupVp.setCurrentItem(0, false);
    }
}
