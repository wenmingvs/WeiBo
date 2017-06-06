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
import com.wenming.weiswift.app.home.contract.HomeContract;
import com.wenming.weiswift.app.home.entity.Group;
import com.wenming.weiswift.app.home.weibo.fragment.WeiBoFragment;
import com.wenming.weiswift.utils.ToastUtil;

import java.util.List;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View {
    private Toolbar mToolBar;
    private TabLayout mGourpTl;
    private ViewPager mGroupVp;
    private AppCompatActivity mActivity;
    private HomeContract.Presenter mPresenter;

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
        mActivity.setSupportActionBar(mToolBar);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initTabViewPager(List<Group> groups) {
        GroupPagerAdapter mAdapter = new GroupPagerAdapter(mActivity.getSupportFragmentManager());
        for (int i = 0; i < groups.size(); i++) {
            mAdapter.addFragment(new WeiBoFragment(), getString(R.string.main_tab_default));
        }
        mGroupVp.setAdapter(mAdapter);
        mGourpTl.setupWithViewPager(mGroupVp);
        //默认展示第一个
        mGroupVp.setCurrentItem(0, false);
    }

    @Override
    public void showServerMessage(String text) {
        ToastUtil.showShort(mContext, text);
    }

    @Override
    public void showLoading() {
        showLoadingDialog(R.string.home_request_groups_ing, false);
    }

    @Override
    public void dismissLoading() {
        dissLoadingDialog();
    }

    @Override
    public void showRetryBg() {

    }

    @Override
    public void hideRetryBg() {

    }

    @Override
    public void showNoneNetWork() {
        ToastUtil.showShort(mContext, R.string.common_none_network);
    }
}
